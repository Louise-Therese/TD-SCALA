Object du Programme: 

Ce programme permet de rechercher des mots-clés sur Wikipédia et de récupérer des informations comme
le nombre de page.

Fonctionnalités: 
    Recherche de pages Wikipédia à partir d'un mot-clé spécifié.
    Limite le nombre de pages retournées.
    Récupère les titres des pages et le nombre de mots de chaque page.
    Calcule le nombre total de mots pour les pages récupérées.

Comment exécuter ?

Compilez le programme en utilisant sur scala,  sbt :

 >compile

Exécutez le programme en spécifiant un mot-clé à rechercher :

Commande

run --limit <n> <keyword>

Remplacez <keyword> par le mot-clé de votre choix.
Exemples d'utilisation

    Recherche du mot-clé "Scala" avec une limite de 17 pages :

Commande

sbt "run Scala --limit 17"

    Recherche du mot-clé "Programming" sans limite de pages (limite par défaut : 10) :
Commande

sbt "run Programming"

Tests unitaires

Des tests unitaires sont inclus pour vérifier le bon fonctionnement des différentes fonctionnalités du programme. Vous pouvez exécuter les tests avec la commande suivante :

Commande
sbt test
