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
* Pour détecter un échec, il faut parcourir toutes les pièces et regarder pour chacune si la case du Roi est dans les cases accessibles ?
* Comment mettre des sons de déplacement, capture, échec, fin de partie, … ?
* Ou faut-il vérifier qu’après un coup, il n’y a pas de position d’échec ? actuellement c’est dans DecoratorCasesAccessibles. Peut etre dans Jeu dans la méthode coupValide() ?




Enum GameEvent dans modèle et on fait passer dans notifierObserver et en fonction on joue un son.

AppliquerCoup à faire dans la classe Coup à la place de Jeu.

Faire des sous classe de Coup pour les coups spéciaux [appliquer les coups] (roques, en passant) [et dans les decorators faire un truc pour l'affichage des cases accessibles]

Pour la prise en passant, faire un attribut phéromonePiece qui correspond au pion venant se déplacer et donc de laisser sa "trace" sur la case.

Pour la detection des echecs, copier le tableau de l'échiquier et faire la vérification. (car si le coup mange une piece, il y aura un problème au moment de rollback le coup)


Idées du prof : mettre un nouveau décorateur pour gérer le roque