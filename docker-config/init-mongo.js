const nomBd = process.env.MONGO_INITDB_DATABASE;
const utilisateurBd = process.env.MONGO_API_USER;
const motDePasseBd = process.env.MONGO_API_PASSWORD;

if (!utilisateurBd || !motDePasseBd || !nomBd) {
  throw new Error("Les informations nécessaires pour l'utilisateur MongoDB ne sont pas définies.");
}

db = db.getSiblingDB(nomBd);

// Création de l'utilisateur qui sera utilisé par l'API avec les privilèges nécessaires
db.createUser({
  user: utilisateurBd,
  pwd: motDePasseBd,
  roles: [
    {
      role: "readWrite",
      db: nomBd
    }
  ]
});

print(`Utilisateur ${utilisateurBd} créé avec succès dans la base de données ${nomBd}.`);