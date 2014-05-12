DROP TYPE STATETYPE;
DROP FUNCTION FinalState;

create type StateType
as
    object (stateCur CHAR, stateIni CHAR, 
        static function ODCIAggregateInitialize(sctx IN OUT StateType) return NUMBER,
        member function ODCIAggregateIterate(self  IN OUT StateType, value IN CHAR) return NUMBER,
        member function ODCIAggregateTerminate(self IN StateType, returnValue OUT CHAR, flags IN number) return NUMBER,
        member function ODCIAggregateMerge(self IN OUT StateType, ctx2 IN StateType) return NUMBER
    );
/

create or replace type body StateType
is
    static function ODCIAggregateInitialize(sctx IN OUT StateType) return number
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateInitialize');
            sctx := StateType(null, null);
            return ODCIConst.Success;
        end;
    member function ODCIAggregateIterate(self IN OUT StateType, value IN CHAR) return number
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
    member function ODCIAggregateTerminate(self IN StateType, returnValue OUT CHAR, flags IN number) return number
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateTerminate');
            returnValue := self.stateCur;
            return ODCIConst.Success;
        end;
    member function ODCIAggregateMerge(self IN OUT StateType, ctx2 IN StateType) return number
    is
        begin
            DBMS_OUTPUT.PUT_LINE('ODCIAggregateMerge: self: ' || self.stateCur || ' ctx2: ' || ctx2.stateCur);
            return ODCIConst.Success;
        end;
    end;
/

CREATE FUNCTION FinalState (input CHAR) RETURN CHAR 
PARALLEL_ENABLE AGGREGATE USING StateType;
/
    
select count(*), KTU_PK_PK1, FinalState(KTU_STATE) 
FROM ktu_mytable
group by KTU_PK_PK1
order by KTU_STATE

select *
from ( 
    select listagg(ktu_state) within group (order by KTU_ID) as KTU_STATE, KTU_PK_PK1
    from ktu_mytable
    group by ktu_pk_pk1
) kt
left join mytable t on kt.KTU_PK_PK1 = t.pk1
