drop table "OCABI"."KTU_MYTABLE";
drop table "OCABI"."MYTABLE";
drop sequence "OCABI"."KTU_SEQ";

  CREATE TABLE "OCABI"."KTU_MYTABLE" 
   (	
  "KTU_ID" NUMBER,
  "KTU_TS" TIMESTAMP NOT NULL ENABLE, 
	"KTU_STATE" CHARACTER NOT NULL ENABLE, 
	"KTU_PK_PK1" NUMBER NOT NULL ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
  /
  
  CREATE SEQUENCE  "OCABI"."KTU_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;
  / 

  
CREATE TABLE "OCABI"."MYTABLE" 
   (	"PK1" NUMBER NOT NULL ENABLE, 
	"VALUE" VARCHAR2(20 BYTE), 
	 CONSTRAINT "MYTABLE_PK" PRIMARY KEY ("PK1")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
/ 

CREATE OR REPLACE TRIGGER "OCABI"."KTU_TRG_MYTABLE_I" 
AFTER INSERT ON MYTABLE
FOR EACH ROW 
BEGIN
    --DELETE FROM KTU_MYTABLE WHERE PK1 = :NEW.PK1;
    INSERT INTO KTU_MYTABLE (KTU_ID, KTU_TS, KTU_STATE, KTU_PK_PK1) VALUES (KTU_SEQ.nextval, systimestamp, 'I', :NEW.PK1);
END;
/

  CREATE OR REPLACE TRIGGER "OCABI"."KTU_TRG_MYTABLE_U" 
AFTER UPDATE ON MYTABLE
FOR EACH ROW 
BEGIN
    --DELETE FROM KTU_MYTABLE WHERE PK1 = :NEW.PK1;
    INSERT INTO KTU_MYTABLE (KTU_ID, KTU_TS, KTU_STATE, KTU_PK_PK1) VALUES (KTU_SEQ.nextval, systimestamp, 'U', :NEW.PK1);
END;
/
 

CREATE OR REPLACE TRIGGER "OCABI"."KTU_TRG_MYTABLE_D" 
BEFORE DELETE ON MYTABLE
FOR EACH ROW 
BEGIN
    --DELETE FROM KTU_MYTABLE WHERE PK1 = :OLD.PK1;
    INSERT INTO KTU_MYTABLE (KTU_ID, KTU_TS, KTU_STATE, KTU_PK_PK1) VALUES (KTU_SEQ.nextval, systimestamp, 'D', :OLD.PK1);
END;
/

ALTER TRIGGER "OCABI"."KTU_TRG_MYTABLE_I" ENABLE;
ALTER TRIGGER "OCABI"."KTU_TRG_MYTABLE_U" ENABLE;
ALTER TRIGGER "OCABI"."KTU_TRG_MYTABLE_D" ENABLE;
 