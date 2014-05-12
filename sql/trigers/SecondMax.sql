create type SecondMaxImpl
as
  object
  (
    max    NUMBER, -- highest value seen so far
    secmax NUMBER, -- second highest value seen so far
    static
  function ODCIAggregateInitialize
    (
      sctx IN OUT SecondMaxImpl)
    return number,
    member function ODCIAggregateIterate
    (
      self  IN OUT SecondMaxImpl,
      value IN number)
    return number,
    member function ODCIAggregateTerminate
    (
      self IN SecondMaxImpl,
      returnValue OUT number,
      flags IN number)
    return number,
    member function ODCIAggregateMerge
    (
      self IN OUT SecondMaxImpl,
      ctx2 IN SecondMaxImpl)
    return number );
  /
  
  
  
create or replace type body SecondMaxImpl
is
  static
function ODCIAggregateInitialize
  (
    sctx IN OUT SecondMaxImpl)
  return number
is
begin
  sctx := SecondMaxImpl(0, 0);
  return ODCIConst.Success;
end;
member function ODCIAggregateIterate
  (
    self  IN OUT SecondMaxImpl,
    value IN number)
  return number
is
begin
  if value       > self.max then
    self.secmax := self.max;
    self.max    := value;
  elsif value    > self.secmax then
    self.secmax := value;
  end if;
  return ODCIConst.Success;
end;
member function ODCIAggregateTerminate
  (
    self IN SecondMaxImpl,
    returnValue OUT number,
    flags IN number)
  return number
is
begin
  returnValue := self.secmax;
  return ODCIConst.Success;
end;
member function ODCIAggregateMerge
  (
    self IN OUT SecondMaxImpl,
    ctx2 IN SecondMaxImpl)
  return number
is
begin
  if ctx2.max      > self.max then
    if ctx2.secmax > self.secmax then
      self.secmax := ctx2.secmax;
    else
      self.secmax := self.max;
    end if;
    self.max    := ctx2.max;
  elsif ctx2.max > self.secmax then
    self.secmax := ctx2.max;
  end if;
  return ODCIConst.Success;
end;
end;
/

CREATE FUNCTION SecondMax (input NUMBER) RETURN NUMBER 
PARALLEL_ENABLE AGGREGATE USING SecondMaxImpl;
/

select SecondMax(pk1) FROM mytable group by value;