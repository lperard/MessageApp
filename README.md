# POO Project : MessageApp

## Prérequis:

- Installer une version récente de la JDK
- Télécharger ce projet ou créer une copie avec "git clone"

## Manuel d'utilisation

### Lancer l’application

Pour lancer l’application de messagerie, il suffit d’exécuter le script bash MessageApp.sh (soit en lui donnant des droits d’exécution soit en se plaçant dans le dossier et en lançant la commande :

./MessageApp.sh

### Se connecter

Au lancement de l’application, une première fenêtre s’ouvre. L’utilisateur est invité à entrer un pseudo et à appuyer sur un des deux boutons en fonction du réseau sur lequel il se trouve actuellement (“Connexion” s’il est sur le réseau local de l’INSA ou “Connexion remote” dans tous les autres cas) ou presser la touche ENTER du clavier (qui correspond à une connexion en local) pour se connecter avec ce pseudo.

Les contraintes sur le choix du pseudo sont :
- pas de pseudo vide
- pas de pseudo avec plus de 16 caractères
- pas de pseudo identique à l’un des pseudos déjà utilisé par les autres utilisateurs connectés

Si l’utilisateur ne respecte pas une de ces contraintes, une fenêtre de dialogue apparaît alors et lui indique son erreur. Une liste des pseudos des autres utilisateurs connectés se trouve sur la gauche de la fenêtre afin de faciliter le choix.

### Ouvrir/Fermer une session de clavardage

Une fois le choix du pseudo fait et la connexion établie, une nouvelle fenêtre apparaît. Sur la gauche se trouve une liste des utilisateurs connectés.

Pour ouvrir une session de clavardage avec un utilisateur il suffit d’appuyer sur le pseudo de cet utilisateur dans cette même liste. Un onglet s’ouvrira alors sur la partie droite de la fenêtre, affichant l’historique de la conversation avec cet utilisateur et un champ pour lui envoyer des messages.

Le fonctionnement des onglets est assez intuitif, on peut changer d’onglet en cliquant sur un autre, on peut fermer un onglet en cliquant sur la petite croix à côté du pseudo de l’utilisateur au niveau du titre de l’onglet… On peut également changer d’onglet en cliquant sur un autre utilisateur dans la liste des utilisateurs connectés.

### Envoyer un message

Pour envoyer un message il suffit de remplir le champ destiné à cet effet dans l’onglet correspondant au destinataire souhaité. Il faut alors appuyer sur le bouton “Envoyer” ou tout simplement presser la touche Enter du clavier.

Si on cherche à envoyer un message vide, une petite fenêtre de dialogue s’ouvrira, avertissant l’utilisateur et lui demandant de confirmer ou d’annuler son action. A noter également que des messages contenant des apostrophes et/ou guillemets passent sans problème grâce à notre utilisation de requêtes préparées au niveau de la base de données.

Au niveau de l’affichage d’un message, la couleur du bloc qui encadre le message diffère en fonction de si nous sommes la source ou le destinataire de celui-ci. Il y a également un petit système de notification : lorsqu’on reçoit un message un astérisque apparaît à côté du pseudo au niveau du titre de l’onglet et  si aucun onglet ne correspond à l’expéditeur celui-ci est ouvert.

### Envoyer une image ou un fichier

Si l’on souhaite envoyer un image ou un fichier, le processus est sensiblement le même. Il suffit d’appuyer sur le bouton “Importer” en bas à droite de la fenêtre. Une fenêtre de sélection apparaît alors, nous permettant de parcourir notre PC à la recherche du fichier que l’on souhaite expédier.

Au niveau de l’affichage, une image apparaîtra de la même manière qu’un message mais pour un fichier, un bouton apparaît et offre à l’utilisateur de l’enregistrer à l’endroit de son choix s’il clique dessus.

### Changer de pseudo

Pour changer de pseudo, il suffit de cliquer sur le bouton en haut à gauche de la fenêtre. On revient alors à la première fenêtre d’identification et l’on peut choisir à nouveau son pseudo. A noter que l’on est toujours connecté à ce moment là et donc que les autres utilisateurs peuvent toujours nous envoyer des messages. De plus, lors du changement de pseudo, la liste des utilisateurs connectés et les éventuels onglets concernant l’utilisateur ayant changé de pseudo seront modifiés en conséquence.

### Se déconnecter

Pour se déconnecter il suffit d’appuyer sur la croix en haut à droite de la fenêtre.

## Auteurs :

- Matthieu Lacote
- Lucas Perard
- Rafael Achega
