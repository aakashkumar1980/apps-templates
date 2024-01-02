# STATE MANAGEMENT: Concepts
### Local State
- useState / useReducer (for complex usecase)
### Cross Component State
- useState / useReducer (for complex usecase) -> props
### App-wide State
- useState / useReducer (for complex usecase) -> props
- useContext 
  - can have multiple context across an application, i.e. AuthenticationContext, DataContext etc.
  - performance is slow.
- Redux
  - only one allowed for an application.
  - great performance

<br/>
<br/>

# STATE MANAGEMENT: Implementations
## REDUCER
API() | Functions() => &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;variableDispatcher &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(action) -> &nbsp;&nbsp;&nbsp;&nbsp;[variableReducer] => &nbsp;&nbsp;&nbsp;&nbsp;VARIABLE<br/>
&nbsp;(APIServices.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(DataContextProvider.tsx)&nbsp;&nbsp;&nbsp;(Actions.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Reducer.tsx)<br/>
where,
- API(): executes the api calls.
- <u>WRAPPED IN {DataContextProvider.DataContext} so that it is available to all application.</u>
- - Functions(): wrapper the API calls, passes the variableDispatcher argument.<br/><br/>
- - variableDispatcher: passes the API's result to the variableReducer.
- - variableReducer: merges the API's result with the existing values of the VARIABLE.<br/><br/>
- VARIABLE: this is the variable which is used across the application.

```sh
addRecordAPI   -> recordsListDispatcher [recordsListReducer] => recordsList
```
<br/>

## REDUX
API() => &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;variableStore (dispatch action) -> [variableReducer] => &nbsp;&nbsp;&nbsp;&nbsp;VARIABLE <br/>
(APIServices.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Store.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Actions.tsx)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Reducer.tsx)<br/>
where,
- API(): executes the api calls.
- - variableStore is tied to the variableReducer and it passes the API's result to the variableReducer.
- - variableReducer: merges the API's result with the existing values of the VARIABLE.<br/><br/>
- VARIABLE: this is the variable which is used across the application.
```sh
getRecordsAPI   -> recordsListStore [recordsListReducer] => recordsList
deleteRecordAPI
```