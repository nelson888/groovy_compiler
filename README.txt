Ce projet compile avec gradle. Si vous avez gradle installé, utilisez la commande (sur Linux):
```
./gradlew uberjar
```
Cela va construire un fichier .jar dans le dossier build/libs/.



Ce programme s'execute comme un programme java, pour l'executer, utilisez la commande
```
java -jar compiler-1.0.jar ./file1 ./file2 ./file3 ...
```

WARNING
Si vous avez une version de java inférieure ou égale à 8, il n'y a pas de problème.
Si vous avez une version de Java supérieure ou égale à 9, vous aurez des Warnings tel que:
'WARNING: An illegal reflective access operation has occurred'
Mais le programme fonctionnera toujours. Dans ce cas, veillez à ce que les arguments que vous donnez
au programme soient bien des chemins, et non des noms de fichiers. Par exemple

```
java -jar compiler-1.0.jar file1
```
ne marchera pas, MAIS
```
java -jar compiler-1.0.jar ./file1
```
marchera.




Ce programme attend un ou plusieurs fichier en input.

Pour chaque fichier, si le programme est bien écrit, il va créer un nouveau
fichier contenant le code compilé, dans le même repertoire, dans un fichier filename.code (par exemple
si le nom du fichier en entrée était 'programme1', le code compilé sera dans un fichier 'programme1.code').

Si une erreur est survenue (syntaxique, lexicale...) alors un message d'erreur s'affichera à la sortie standard
indiquant le type d'erreur, la cause et la ligne et colonne.



Il y a des fonctions précompilées (qu'on peut voir dans le fichier src/main/ressources/functions) donc on ne peut écrire
dans le code des fonctions avec le nom:
- power
- malloc


Ce compilateur compile un programme. Un programme est une liste de fonctions,
les unes à la suite des autres. Il faut qu'il y ait une fonction main(), sinon le compilateur
ne compilera pas le programme et affichera un message d'erreur.


Exemple de programme valide:


add1(a) {
  return a + 1;
}

main() {
  print(add1(1));

}