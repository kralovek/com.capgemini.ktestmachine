package com.capgemini.ktestmachine.component.diffmanager.databasetrigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.database.ConstantsDatabase;
import com.capgemini.ktestmachine.utils.database.UtilsDatabase;

public class DiffManagerDatabaseTrigger extends ADiffManagerDatabaseTriggerFwk
		implements DiffManager, ConstantsDatabase {
	private static final Logger LOGGER = Logger
			.getLogger(DiffManagerDatabaseTrigger.class);

	private static final DateFormat DATE_FORMAT_TODATE = new SimpleDateFormat(
			"yyyyMMdd HHmmss SSS");
	private static final DateFormat DATE_FORMAT_STRING = new SimpleDateFormat(
			"HH:mm:ss.SSS dd/MM/yyyy");

	private static final String KTM_PREFIX_PK = "KTM_PK_";

	private static final char STATE_UNKNOWN = '-';
	private static final char STATE_INSERT = 'I';
	private static final char STATE_UPDATE = 'U';
	private static final char STATE_DELETE = 'D';
	private static final char STATE_IGNOR = 'X';

	private class PK implements Comparable<PK> {
		private List<String> pks;
		private List<ItemCruid> itemCruids = new ArrayList<ItemCruid>();

		public PK(List<String> pks) {
			this.pks = pks;
		}

		public List<ItemCruid> getItemCruids() {
			return itemCruids;
		}

		private int compareToString(String s1, String s2) {
			return s1 != null && s2 != null ? s1.compareTo(s2)
					: s1 != null ? +1 : s2 != null ? -1 : 0;
		}

		public int compareTo(List<String> pks) {
			if (this.pks.size() != pks.size()) {
				return new Integer(this.pks.size()).compareTo(pks.size());
			}
			for (int i = 0; i < this.pks.size(); i++) {
				int retval = compareToString(this.pks.get(i), pks.get(i));
				if (retval != 0) {
					return retval;
				}
			}
			return 0;
		}

		public int compareTo(PK pk) {
			return compareTo(pk.pks);
		}

		public boolean equals(Object object) {
			if (!(object instanceof PK)) {
				return false;
			}
			return compareTo((PK) object) == 0;
		}

		public String toString() {
			return toStringValuesPK(pks);
		}
	}

	public List<Group> loadDiffs(List<Group> groupStates) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			List<Group> groups = new ArrayList<Group>();

			Connection connection = null;
			try {
				connection = dataSource.getConnection();

				for (TableInfo tableInfo : tableInfos) {
					Group groupState = findGroup(groupStates,
							tableInfo.getName());
					IndexImpl index = groupState != null ? (IndexImpl) groupState
							.getLastIndex() : null;
					Group group = readDiffTable(connection, tableInfo, index);
					groups.add(group);

					cleanTriggeredTable(index, tableInfo, connection);
				}

				connection.close();
				connection = null;
			} catch (SQLException ex) {
				throw new TechnicalException(ex);
			} finally {
				UtilsDatabase.getInstance().closeResource(connection);
			}
			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	public List<Group> loadCurrents() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			List<Group> groups = new ArrayList<Group>();

			Connection connection = null;
			try {
				connection = dataSource.getConnection();

				for (TableInfo tableInfo : tableInfos) {
					Group group = readCurrentTable(connection, tableInfo);
					groups.add(group);
				}

				connection.close();
				connection = null;
			} catch (SQLException ex) {
				throw new TechnicalException(ex);
			} finally {
				UtilsDatabase.getInstance().closeResource(connection);
			}
			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Group findGroup(List<Group> groups, String name) {
		for (Group group : groups) {
			if (name.equals(group.getName())) {
				return group;
			}
		}
		return null;
	}

	private Group readCurrentTable(Connection connection, TableInfo tableInfo)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		String triggeredTableSchemaName = null;
		try {
			if ("M2OVOIE"
					.equalsIgnoreCase(tableInfo.getName())) {
				int k = 0;
			}


			Statement statement = null;
			ResultSet resultSet = null;
			triggeredTableSchemaName = getTriggeredTableSchemaName(tableInfo);
			String query = "SELECT max(KTM_TS) FROM "
					+ triggeredTableSchemaName;
			GroupImpl group = new GroupImpl(tableInfo.getName());

			try {
				LOGGER.debug("QUERY: " + query);

				statement = connection.createStatement();
				resultSet = statement.executeQuery(query);

				Timestamp index = null;
				if (resultSet.next()) {
					try {
						index = resultSet.getTimestamp(1);
						LOGGER.debug("max ID: " + index);
					} catch (SQLException ex) {
						throw new TechnicalException(
								"The KTM_TS is NULL ot is not a TIMESTAMP in the table: "
										+ triggeredTableSchemaName);
					}
				}

				IndexImpl indexImpl = new IndexImpl();
				indexImpl.setTimestamp(index);

				group.setLastIndex(indexImpl);

				resultSet.close();
				statement.close();
				statement = null;

				LOGGER.trace("OK");
				return group;
			} catch (SQLException ex) {
				if (ex.getErrorCode() == DB_ERROR_TABLE_NOT_EXIST) {
					throw new TechnicalException("The table does not exist: "
							+ triggeredTableSchemaName, ex);
				} else {
					throw new TechnicalException(
							"Cannot execute the statement: " + query, ex);
				}
			} finally {
				UtilsDatabase.getInstance().closeResource(resultSet);
				UtilsDatabase.getInstance().closeResource(statement);
			}
		} finally {
			LOGGER.trace("END");
		}
	}

	private static Date roundDateToMs(Date date) {
		return new Date(date.getTime() + 1);
	}

	private String getTableSchemaName(TableInfo tableInfo) {
		return (tableInfo.getSchema() != null ? tableInfo.getSchema() + "."
				: "") + tableInfo.getName();
	}

	private String getTriggeredTableSchemaName(TableInfo tableInfo) {
		return (tableInfo.getKtmSchema() != null ? tableInfo.getKtmSchema()
				+ "." : "")
				+ tableInfo.getKtmNamePrefix() + tableInfo.getName();
	}

	private void cleanTriggeredTable(IndexImpl index, TableInfo tableInfo,
			Connection connection) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			PreparedStatement statement = null;

			String triggeredTableName = getTriggeredTableSchemaName(tableInfo);
			String query = "DELETE FROM " + triggeredTableName
					+ " WHERE KTM_TS < ?";

			try {
				LOGGER.debug("QUERY: " + query);

				statement = connection.prepareStatement(query);
				statement.setTimestamp(1, index.getTimestamp());

				statement.executeUpdate();
				statement.close();

			} catch (SQLException ex) {
				throw new TechnicalException(ex);
			} finally {
				UtilsDatabase.getInstance().closeResource(statement);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private Group readDiffTable(Connection connection, TableInfo tableInfo,
			IndexImpl index) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.debug("Table: " + tableInfo.getName());
			
			if ("M2OVOIE"
					.equalsIgnoreCase(tableInfo.getName())) {
				int k = 0;
			}

			List<PK> listPks = new ArrayList<PK>();
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try {
				if (index.getTimestamp() == null) {
					index.setTimestamp(new Timestamp(0L));
				}
				
				GroupImpl group = new GroupImpl(tableInfo.getName());

				String triggeredTableSchemaName = getTriggeredTableSchemaName(tableInfo);
				String tableSchemaName = getTableSchemaName(tableInfo);

				String sqlListPKs = toSqlListKtuPKs(tableInfo);

				String query = "" //
						+ " SELECT * FROM (" //
						+ "   SELECT KTM_STATE, KTM_TS, "
						+ sqlListPKs //
						+ "   FROM "
						+ triggeredTableSchemaName //
						+ "   WHERE KTM_TS > ?"
						+ " ) tt" //
						+ " LEFT JOIN " + tableSchemaName
						+ " t ON "
						+ toSqlListJoinKtuPKs(tableInfo, "t", "tt") //
						+ " ORDER BY tt.KTM_TS ASC" //
				;

				String queryRep = query.replaceFirst("\\?",
						toString(index.getTimestamp()));

				statement = connection.prepareStatement(query);

				Timestamp indexTimestamp = index.getTimestamp();
				LOGGER.debug("timestamp: " + indexTimestamp);
				statement.setTimestamp(1, indexTimestamp);

				LOGGER.debug("QUERY: " + queryRep);

				resultSet = statement.executeQuery();
				ResultSetMetaData metaData = resultSet.getMetaData();

				while (resultSet.next()) {
					ItemCruid itemCruid = new ItemCruid(patternDate);

					String state = resultSet.getString(1);
					Status status = castStatus(state);
					itemCruid.setStatus(status);

					Timestamp timestamp = resultSet.getTimestamp(2);
					IndexImpl indexImpl = new IndexImpl();
					indexImpl.setTimestamp(timestamp);
					itemCruid.setIndex(indexImpl);

					List<String> valuesPK = new ArrayList<String>();

					for (int i = 3; i <= metaData.getColumnCount(); i++) {
						boolean isPK = false;
						String columnName = metaData.getColumnName(i);
						if (i <= 2 + tableInfo.getColumnsPK().size()) {
							columnName = columnName.substring(KTM_PREFIX_PK
									.length());
							isPK = true;
						} else {
							if (isPK(tableInfo, columnName)) {
								continue;
							}
						}

						Object valueFinal = null;

						switch (metaData.getColumnType(i)) {
						case Types.DATE:
						case Types.TIME:
						case Types.TIMESTAMP: {
							Date valueDate = resultSet.getTimestamp(i);
							valueFinal = valueDate;
							if (isPK) {
								valuesPK.add(valueDate != null ? DATE_FORMAT_STRING
										.format(valueDate) : null);
							}
							break;
						}
						case Types.DECIMAL:
						case Types.INTEGER:
						case Types.NUMERIC:
						case Types.DOUBLE: {
							valueFinal = resultSet.getObject(i);
							if (isPK) {
								valuesPK.add(valueFinal != null ? valueFinal
										.toString() : null);
							}
							break;
						}
						default: {
							valueFinal = resultSet.getString(i);
							if (isPK) {
								valuesPK.add((String) valueFinal);
							}
						}
						}

						itemCruid.getParameters().put(columnName, valueFinal);
					}
					itemCruid.setName(toStringValuesPK(valuesPK));

					PK pk = removeItemCruidsByPK(listPks, valuesPK);
					if (pk == null) {
						pk = new PK(valuesPK);
					}
					pk.getItemCruids().add(itemCruid);
					listPks.add(pk);
				}

				if (tableInfo.getJoinStatus()) {
					groupByStatus(group, listPks);
				} else {
					groupByResult(group, listPks);
				}

				resultSet.close();
				statement.close();
				statement = null;

				LOGGER.trace("OK");
				return group;
			} catch (SQLException ex) {
				throw new TechnicalException(ex);
			} finally {
				UtilsDatabase.getInstance().closeResource(resultSet);
				UtilsDatabase.getInstance().closeResource(statement);
			}
		} finally {
			LOGGER.trace("END");
		}
	}

	private void groupByStatus(GroupImpl group, List<PK> listPks) {
		for (PK pk : listPks) {
			if (pk.getItemCruids().size() == 1) {
				ItemCruid lastItem = pk.getItemCruids().get(0);
				group.getItems().add(lastItem.toItem());
			} else {
				Status status = adaptStatuses(pk.getItemCruids());
				if (status == null) { // line was added and removed after
					continue;
				}
				ItemCruid lastItem = pk.getItemCruids().get(
						pk.getItemCruids().size() - 1);
				lastItem.setStatus(status);
				group.getItems().add(lastItem.toItem());
			}
		}
	}

	private void groupByResult(GroupImpl group, List<PK> listPks) {
		for (PK pk : listPks) {
			for (ItemCruid itemCruid : pk.getItemCruids()) {
				group.getItems().add(itemCruid.toItem());
			}
		}
	}

	private String toSqlListKtuPKs(TableInfo tableInfo) {
		StringBuffer buffer = new StringBuffer();
		for (String columnPK : tableInfo.getColumnsPK()) {
			if (buffer.length() != 0) {
				buffer.append(',');
			}
			buffer.append(KTM_PREFIX_PK).append(columnPK);
		}
		return buffer.toString();
	}

	private String toSqlListJoinKtuPKs(TableInfo tableInfo, String aliasTable,
			String aliasTriggeredTable) {
		StringBuffer buffer = new StringBuffer();
		for (String columnPK : tableInfo.getColumnsPK()) {
			if (buffer.length() != 0) {
				buffer.append(" AND ");
			}
			buffer.append(aliasTriggeredTable).append('.')
					.append(KTM_PREFIX_PK).append(columnPK).append(" = ")
					.append(aliasTable).append('.').append(columnPK);
		}
		return buffer.toString();
	}

	private String toString(Timestamp timestamp) {
		return "to_timestamp('" + DATE_FORMAT_TODATE.format(timestamp)
				+ "','YYYYMMDD HH24MISS FF')";
	}

	private boolean isPK(TableInfo tableInfo, String column) {
		for (String columnPK : tableInfo.getColumnsPK()) {
			if (column.equalsIgnoreCase(columnPK)) {
				return true;
			}
		}
		return false;
	}

	private static String toStringValuesPK(List<String> valuesPK) {
		if (valuesPK != null) {
			StringBuffer buffer = new StringBuffer();
			for (String valuePK : valuesPK) {
				if (buffer.length() != 0) {
					buffer.append('-');
				}
				buffer.append(valuePK);
			}
			return buffer.toString();
		} else {
			return "-";
		}
	}

	private Status castStatus(String status) {
		if (status == null || status.length() != 1) {
			return Status.UNK;
		}
		switch (status.charAt(0)) {
		case STATE_INSERT:
			return Status.NEW;
		case STATE_UPDATE:
			return Status.UPD;
		case STATE_DELETE:
			return Status.DEL;
		default:
			return Status.UNK;
		}
	}

	private Status adaptStatuses(List<ItemCruid> items) {
		StringBuffer buffer = new StringBuffer();
		for (ItemCruid item : items) {
			if (item.getStatus().equals(Status.NEW)) {
				buffer.append(STATE_INSERT);
			} else if (item.getStatus().equals(Status.UPD)) {
				buffer.append(STATE_UPDATE);
			} else if (item.getStatus().equals(Status.DEL)) {
				buffer.append(STATE_DELETE);
			} else if (item.getStatus().equals(Status.UNK)) {
				buffer.append(STATE_UNKNOWN);
			}
		}
		String states = buffer.toString();

		char stateIni = states.charAt(0);
		char stateCur = stateIni;

		for (int i = 1; i < states.length(); i++) {
			char stateNew = states.charAt(i);

			switch (stateIni) {
			case STATE_INSERT:
				switch (stateCur) {
				case STATE_UNKNOWN:
					return Status.UNK;
				case STATE_INSERT:
					switch (stateNew) {
					case STATE_UPDATE:
						stateCur = STATE_INSERT;
						break;
					case STATE_DELETE:
						stateCur = STATE_IGNOR;
						break;
					default:
						return Status.UNK;
					}
					break;
				case STATE_IGNOR:
					switch (stateNew) {
					case STATE_INSERT:
						stateCur = STATE_INSERT;
						break;
					default:
						return Status.UNK;
					}
					break;
				default:
					return Status.UNK;
				}
				break;
			case STATE_UPDATE:
			case STATE_DELETE:
				switch (stateCur) {
				case STATE_UPDATE:
					switch (stateNew) {
					case STATE_UPDATE:
						stateCur = STATE_UPDATE;
						break;
					case STATE_DELETE:
						stateCur = STATE_DELETE;
						break;
					default:
						return Status.UNK;
					}
					break;
				case STATE_DELETE:
					switch (stateNew) {
					case STATE_INSERT:
						stateCur = STATE_UPDATE;
						break;
					default:
						return Status.UNK;
					}
					break;
				default:
					return Status.UNK;
				}
				break;
			default:
				return Status.UNK;
			}
		}

		switch (stateCur) {
		case STATE_IGNOR:
			return null;
		case STATE_INSERT:
			return Status.NEW;
		case STATE_UPDATE:
			return Status.UPD;
		case STATE_DELETE:
			return Status.DEL;
		}
		return null;
	}

	private PK removeItemCruidsByPK(List<PK> listPks, List<String> ValuesPk) {
		Iterator<PK> iterator = listPks.iterator();
		while (iterator.hasNext()) {
			PK pk = iterator.next();
			if (pk.compareTo(ValuesPk) == 0) {
				iterator.remove();
				return pk;
			}
		}
		return null;
	}
}
