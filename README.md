# Smart Climate - UBO
Authors: Yasmin AIT MAKSENE & Hassina GADA

## Données méteo

### Données journalières
    https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/synop.<aaaammjjhh>.csv
    <aaaammjjhh>:
        aaaa: Année (Ex: 2017)
        mm: Mois (Ex: 02)
        jj: Jour (Ex: 09)
        hh: Heure par tranches de 3h {00, 03, 06, 09, 12, 15, 18, 21}

/!\ Seul les 15 derniers jours sont disponible avec ce format là.

### Données mensuelles
    https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/Archive/synop.<aaaamm>.csv.gz
    <aaaamm>: Depuis janvier 1996 (199601)
/!\ Les données sont au format csv contrairement à ce qui est indiqué sur le site.
 
## Useful links
 - DONNÉES SYNOP ESSENTIELLES OMM - https://donneespubliques.meteofrance.fr/?fond=produit&id_produit=90&id_rubrique=32
 - Descriptif des paramètres de données SYNOP essentielles OMM - https://donneespubliques.meteofrance.fr/client/document/doc_parametres_synop_168.pdf
 - Liste des stations essentielles (format csv) - https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/postesSynop.csv
 - Liste des stations essentielles (format GeoJSON) - https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/postesSynop.json
 