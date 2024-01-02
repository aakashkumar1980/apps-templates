# STATE MANAGEMENT
## Local State
- useState / useReducer (for complex usecase)
## Cross Component State
- useState / useReducer (for complex usecase) -> props
## App-wide State
- useState / useReducer (for complex usecase) -> props
- useContext 
  - can have multiple context across an application, i.e. AuthenticationContext, DataContext etc.
  - performance is slow.
- Redux
  - only one allowed for an application.
  - great performance

<br/>
<br/>

# REDUCER
API() | Functions() => variableDispatcher (action) -> [variableReducer] => VARIABLE <br/>
 (APIServices.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(DataStore.tsx)<br/>
where,
- API(): executes the api calls.<br/>
- <u>WRAPPED IN {DataContextProvider.DataContext} so that it is available to all application.</u>
- - Functions(): wrapper the API calls, passes the variableDispatcher argument.<br/><br/>
- - variableDispatcher: passes the API's result to the variableReducer.
- - variableReducer: merges the API's result with the existing values of the VARIABLE.<br/><br/>
- - VARIABLE: this is the variable which is used acros the application.

```sh
getRecordsAPI
addRecordAPI   -> recordsListDispatcher [recordsListReducer] => recordsList
deleteRecordAPI
```
<br/>

# REDUX
API() => variableStore (dispatch Action) -> [variableReducer] => VARIABLE <br/>
