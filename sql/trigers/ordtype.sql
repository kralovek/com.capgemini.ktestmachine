DROP TYPE ORDTYPE;
DROP FUNCTION ORDState;

create type KTU.ORDTYPE
as
    object (stateCur CHAR, stateIni CHAR, 
        static function ODCIAggregateInitialize(sctx IN OUT KTU.ORDTYPE) return NUMBER,
        member function ODCIAggregateIterate(self  IN OUT KTU.ORDTYPE, value IN CHAR) return NUMBER,
        member function ODCIAggregateTerminate(self IN KTU.ORDTYPE, returnValue OUT CHAR, flags IN NUMBER) return NUMBER,
        member function ODCIAggregateMerge(self IN OUT KTU.ORDTYPE, ctx2 IN KTU.ORDTYPE) return NUMBER
    );
/

create or replace type body KTU.ORDTYPE
is
    static function ODCIAggregateInitialize(sctx IN OUT KTU.ORDTYPE) return number
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateInitialize');
            sctx := KTU.ORDTYPE(null, null);
            RETURN ODCIConst.Success;
        end;
    member function ODCIAggregateIterate(self IN OUT KTU.ORDTYPE, value IN CHAR) return number
    is
            my CHAR;
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateIterate: ' || value);
            IF value IS NULL THEN
                return ODCIConst.Success;
            ELSIF self.stateIni IS NULL THEN
                self.stateCur := value;
                self.stateIni := value;
            ELSIF self.stateIni = 'I' THEN
                IF self.stateCur = 'I' THEN
                    IF value = 'U' THEN
                        self.stateCur := 'I';
                    ELSIF value = 'D' THEN
                        self.stateCur := 'X';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                ELSIF self.stateCur = 'X' THEN
                    IF value = 'I' THEN
                        self.stateCur := 'I';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                END IF;
            ELSIF self.stateIni = 'U' THEN
                IF self.stateCur = 'U' THEN
                    IF value = 'U' THEN
                        self.stateCur := 'U';
                    ELSIF value = 'D' THEN
                        self.stateCur := 'D';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                ELSIF self.stateCur = 'D' THEN
                    IF value = 'I' THEN
                        self.stateCur := 'U';
                    ELSIF value = 'U' THEN
                        self.stateCur := 'U';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                END IF;
            ELSIF self.stateIni = 'D' THEN
                IF self.stateCur = 'U' THEN
                    IF value = 'U' THEN
                        self.stateCur := 'U';
                    ELSIF value = 'D' THEN
                        self.stateCur := 'D';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                ELSIF self.stateCur = 'D' THEN
                    IF value = 'I' THEN
                        self.stateCur := 'U';
                    ELSE
                        self.stateCur := 'E';
                    END IF;
                END IF;
            ELSE 
                self.stateCur := 'E';
            END IF;
            RETURN ODCIConst.Success;
        END;
    member function ODCIAggregateTerminate(self IN KTU.ORDTYPE, returnValue OUT CHAR, flags IN NUMBER) RETURN NUMBER
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateTerminate');
            returnValue := self.stateCur;
            RETURN ODCIConst.Success;
        end;
    member function ODCIAggregateMerge(self IN OUT KTU.ORDTYPE, ctx2 IN KTU.ORDTYPE) RETURN NUMBER
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateMerge: self: ' || self.stateCur || ' ctx2: ' || ctx2.stateCur);
            RETURN ODCIConst.Success;
        end;
    end;
/

CREATE FUNCTION ORDState (input CHAR) RETURN CHAR 
PARALLEL_ENABLE AGGREGATE USING KTU.ORDTYPE;
/
    
select count(*), KTU_PK_DATE_ID, FinalState(KTU_STATE) 
FROM KTU.ft_dmcc_resil
group by KTU_PK_DATE_ID
order by KTU_STATE

