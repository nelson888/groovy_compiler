Ce projet compile avec gradle. Si vous avez gradle installé, utilisez la commande (sur Linux):
```
./gradlew uberjar
```
Cela va construire un fichier .jar.


Ce programme s'execute comme un programme java, pour l'executer, utilisez la commande
```
java -jar compiler-1.0.jar file1 file2 file3 ...
```
Ce programme attend un ou plusieurs fichier en input.
Pour chaque fichier, si le programme est bien écrit, il va créer un nouveau
fichier contenant le code compilé, dans le même repertoire, dans un fichier filename.code (par exemple
si le nom du fichier en entrée était 'programme1', le code compilé sera dans uun fichier 'programme1.code').
Si une erreur est survenue (syntaxique, lexicale...) alors un message d'erreur s'affichera à la sortie standard
indiquant le type d'erreur, la cause et la ligne et colonne.


Il y a des fonctions précompilé (qu'on peut voir dans le fichier src/main/ressources/functions) donc on ne peut écrire
dans le code des fonctions avec le nom:
- power
- malloc