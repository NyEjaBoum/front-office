## Front Office (FO) — Dev 3657
1. **Envoyer le token vers le Back-Office**
	- Tous les appels FO vers l’API BO (ex: `GET /api/reservations`) doivent envoyer le token.
	- Format recommandé : header `X-API-TOKEN: <token>`.

2. **Token dans configuration (FO)**
	- Mettre le token (ou la clé / secret de token) dans un fichier de configuration du Front Office.
	- Tous les appels API côté client doivent lire ce token depuis la configuration et l’envoyer systématiquement.

3. **Main de test (FO)**
	- Créer un `Main` côté Front Office pour tester la vérification du token :
		- Appeler l’API BO avec token valide => doit retourner la liste.
		- Appeler l’API BO sans token / mauvais token => doit être refusé.
		- Appeler l’API BO avec token expiré => doit répondre `token expire`.

4. **Génération token (FO)**
	- Créer un `Main` côté Front Office pour générer un token.
	- Important : le Back Office doit connaître ce token (en base) pour que la validation marche.
		- Option B : insertion directe en base (table `token`) pendant le dev.