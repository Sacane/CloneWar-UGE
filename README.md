# Clonewar 

## Contributors
* Ramaroson Rakotomihamina "Sacane" Johan
* Tellier Quentin

## How to build
* To download dependencies using the following command :
   * On **Linux** :
  ```./mvnw clean install -U```
   * On Windows : ```.\mvnw.cmd clean install```
* To package for production :
  * On **Linux** :
         ```./mvnw package```
  * On Windows : ```.\mvnw.cmd package```
  * The **front** files will be generated in the **src/main/clonewar-front/public/** folder
  * the executable jar will be generated in the **target** folder