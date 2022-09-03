# Hotel-Management
## Evolutionary Algorithm based Hotel Management System<br />
### Relevant links and descriptions: <br />
Abstract EA Workshop idea description - <br />
https://docs.google.com/document/d/1YY0OspEIgzkaTazFEswbjXmCcPQhtOHQ-Assu31cTPs/edit?usp=sharing<br />
EA Workshop: High Level Design - <br />
https://docs.google.com/document/d/1lj0mkhANW8TnzjaNRPEdvkXjUrwZuGFWnpUaQ_BBDtM/edit?usp=sharing<br />

## DB <br>
This is a MySql relational DB based project.
`hoteldb`DB Schema can be installed using recent DB dump located under
this project at src\main\resources\DB\Dump20220806.sql
To Install `hoteldb`DB Schema, make sure to have MySql relational DB installed on you computer.
Run command line: <br>
`mysql -h localhost -uroot -proot  < src\main\resources\DB\Dump20220806.sql`


You might have to remove any <br>
collation: 'utf8mb4_0900_ai_ci' <br>
collate = 'utf8mb4_0900_ai_ci'
prior of running the command.

