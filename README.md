# JavaChess

### A faire

Toutes les taches à faire sont dans les TODO.

* Detection d'échec / échec et mat
* Coups spéciaux (roque, promotion, en passant)

#### Extensions : 

* Ajouter des sons de déplacement, erreur, capture, promotion, ...
* Affichage de l'historique des coups à droite, ainsi que le joueur (sa couleur) à qui c'est le tour. Et aussi un chronomètre du temps de jeu.
* Fin de partie par échiquier identique 3 fois
* Ajouter une IA pour jouer contre nous
* Menu de selection du mode de jeu au début.
* Développer la version console du jeu : VueControllerConsole.java

#### Questions :

* Faut il faire des Decorator pour pion, cavalier et roi ?
* Etre sur que DecLigne et DecDiag, le « Dec » veut bien dire Decorator. Sinon changer le noms des fichiers.
* Pour détecter un échec, il faut parcourir toutes les pièces et regarder pour chacune si la case du Roi est dans les cases accessibles ?
* Comment mettre des sons de déplacement, capture, échec, fin de partie, … ?
* Ou faut-il vérifier qu’après un coup, il n’y a pas de position d’échec ? actuellement c’est dans DecoratorCasesAccessibles. Peut etre dans Jeu dans la méthode coupValide() ?
