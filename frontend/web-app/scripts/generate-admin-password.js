#!/usr/bin/env node

/**
 * Script para generar hash de contraseña de administrador
 * Uso: node scripts/generate-admin-password.js tu_contraseña
 */

const bcrypt = require('bcryptjs');

const password = process.argv[2];

if (!password) {
  console.error('❌ Error: Debes proporcionar una contraseña');
  console.log('Uso: node scripts/generate-admin-password.js tu_contraseña');
  process.exit(1);
}

if (password.length < 8) {
  console.error('❌ Error: La contraseña debe tener al menos 8 caracteres');
  process.exit(1);
}

const hash = bcrypt.hashSync(password, 10);

console.log('\n✅ Hash generado exitosamente:\n');
console.log(hash);
console.log('\nAgrega esta línea a tu .env.local:');
console.log(`ADMIN_PASSWORD_HASH=${hash}`);
console.log('\n');
