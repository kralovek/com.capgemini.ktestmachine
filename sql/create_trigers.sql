
CREATE OR REPLACE TRIGGER TRG_M2OEQUIPEMENT_I AFTER INSERT ON M2OEQUIPEMENT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OEQUIPEMENT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OEQUIPEMENT_U AFTER UPDATE ON M2OEQUIPEMENT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OEQUIPEMENT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OEQUIPEMENT_D BEFORE DELETE ON M2OEQUIPEMENT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OEQUIPEMENT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OALARME_I AFTER INSERT ON M2OALARME FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OALARME (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OALARME_U AFTER UPDATE ON M2OALARME FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OALARME (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OALARME_D BEFORE DELETE ON M2OALARME FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OALARME (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OHISTORIQUEDESECHANGES_I AFTER INSERT ON M2OHISTORIQUEDESECHANGES FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OHISTORIQUEDESECHANGES (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OHISTORIQUEDESECHANGES_U AFTER UPDATE ON M2OHISTORIQUEDESECHANGES FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OHISTORIQUEDESECHANGES (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OHISTORIQUEDESECHANGES_D BEFORE DELETE ON M2OHISTORIQUEDESECHANGES FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OHISTORIQUEDESECHANGES (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OERREUR_I AFTER INSERT ON M2OERREUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OERREUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OERREUR_U AFTER UPDATE ON M2OERREUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OERREUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OERREUR_D BEFORE DELETE ON M2OERREUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OERREUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OREJETSPREPAR_I AFTER INSERT ON M2OREJETSPREPAR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETSPREPAR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OREJETSPREPAR_U AFTER UPDATE ON M2OREJETSPREPAR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETSPREPAR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OREJETSPREPAR_D BEFORE DELETE ON M2OREJETSPREPAR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETSPREPAR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OADRESSE_I AFTER INSERT ON M2OADRESSE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OADRESSE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OADRESSE_U AFTER UPDATE ON M2OADRESSE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OADRESSE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OADRESSE_D BEFORE DELETE ON M2OADRESSE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OADRESSE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OVOIE_I AFTER INSERT ON M2OVOIE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OVOIE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OVOIE_U AFTER UPDATE ON M2OVOIE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OVOIE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OVOIE_D BEFORE DELETE ON M2OVOIE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OVOIE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OCOMMUNE_I AFTER INSERT ON M2OCOMMUNE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMMUNE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCOMMUNE_U AFTER UPDATE ON M2OCOMMUNE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMMUNE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCOMMUNE_D BEFORE DELETE ON M2OCOMMUNE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMMUNE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJMSM_I AFTER INSERT ON M2OCMDBMAJMSM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJMSM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJMSM_U AFTER UPDATE ON M2OCMDBMAJMSM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJMSM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJMSM_D BEFORE DELETE ON M2OCMDBMAJMSM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJMSM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJHR_I AFTER INSERT ON M2OCMDBMAJHR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJHR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJHR_U AFTER UPDATE ON M2OCMDBMAJHR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJHR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCMDBMAJHR_D BEFORE DELETE ON M2OCMDBMAJHR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCMDBMAJHR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OLIENRADIO_I AFTER INSERT ON M2OLIENRADIO FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OLIENRADIO (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OLIENRADIO_U AFTER UPDATE ON M2OLIENRADIO FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OLIENRADIO (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OLIENRADIO_D BEFORE DELETE ON M2OLIENRADIO FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OLIENRADIO (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_IMTICKET_I AFTER INSERT ON IMTICKET FOR EACH ROW
BEGIN 
    INSERT INTO KTM_IMTICKET (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_IMTICKET_U AFTER UPDATE ON IMTICKET FOR EACH ROW
BEGIN 
    INSERT INTO KTM_IMTICKET (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_IMTICKET_D BEFORE DELETE ON IMTICKET FOR EACH ROW
BEGIN 
    INSERT INTO KTM_IMTICKET (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OPROJETMAINTENANCE_I AFTER INSERT ON M2OPROJETMAINTENANCE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPROJETMAINTENANCE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OPROJETMAINTENANCE_U AFTER UPDATE ON M2OPROJETMAINTENANCE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPROJETMAINTENANCE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OPROJETMAINTENANCE_D BEFORE DELETE ON M2OPROJETMAINTENANCE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPROJETMAINTENANCE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OREJETMDM_I AFTER INSERT ON M2OREJETMDM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETMDM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OREJETMDM_U AFTER UPDATE ON M2OREJETMDM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETMDM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OREJETMDM_D BEFORE DELETE ON M2OREJETMDM FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OREJETMDM (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OPOINTCOMPTAGE_I AFTER INSERT ON M2OPOINTCOMPTAGE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPOINTCOMPTAGE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OPOINTCOMPTAGE_U AFTER UPDATE ON M2OPOINTCOMPTAGE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPOINTCOMPTAGE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OPOINTCOMPTAGE_D BEFORE DELETE ON M2OPOINTCOMPTAGE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OPOINTCOMPTAGE (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OCOMPTEUR_I AFTER INSERT ON M2OCOMPTEUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMPTEUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCOMPTEUR_U AFTER UPDATE ON M2OCOMPTEUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMPTEUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCOMPTEUR_D BEFORE DELETE ON M2OCOMPTEUR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCOMPTEUR (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/
--------------------------------------------

CREATE OR REPLACE TRIGGER TRG_M2OCONTRAT_I AFTER INSERT ON M2OCONTRAT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCONTRAT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'I', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCONTRAT_U AFTER UPDATE ON M2OCONTRAT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCONTRAT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'U', :NEW.SYSID);
END;
/
CREATE OR REPLACE TRIGGER TRG_M2OCONTRAT_D BEFORE DELETE ON M2OCONTRAT FOR EACH ROW
BEGIN 
    INSERT INTO KTM_M2OCONTRAT (KTM_TS, KTM_STATE, KTM_PK_SYSID) VALUES (SYSTIMESTAMP, 'D', :OLD.SYSID);
END;
/







