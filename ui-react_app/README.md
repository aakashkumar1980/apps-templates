# REDUCER
FUNCTION() -> variableDispatcher [variableReducer] => VARIBALE <br/>
where,
- FUNCTION: executes the business logic.<br/>
- variableDispatcher: passes the FUNCTION's result to the variableReducer.<br/>
- variableReducer: merges the FUNCTION's result with the existing values of the VARIABLE.<br/>
- VARIBALE: finally its value is calculated.<br/>

```sh
getRecordsAPI
addRecordAPI   -> recordsListDispatcher [recordsListReducer] => recordsList
deleteRecordAPI
```

