// Esta función genera una clave criptográfica utilizando el algoritmo SHA-256
// a partir de un secreto definido, y devuelve los primeros 16 bytes de la clave resultante.
const getKey = async () => {
    const secretKey = 'SECRETO'
    const encoder = new TextEncoder();
    const hashedKey = await window.crypto.subtle.digest(
      {
        name: "SHA-256",
      },
      encoder.encode(secretKey)
    );
    return new Uint8Array(hashedKey).slice(0, 16);
}

// Esta función realiza la operación de descifrado de un texto cifrado en formato base64
// utilizando el algoritmo AES-CBC y una clave generada mediante la función getKey().
// Devuelve el texto descifrado.
export async function decrypt(ciphertext) {
    // Crea un decodificador de texto
    const decoder = new TextDecoder();
    // Obtiene la clave criptográfica mediante la función getKey()
    const keyMaterial = await getKey(); 
      // Importa la clave en el formato "raw" para descifrado AES-CBC
    const key = await window.crypto.subtle.importKey(
        "raw",
        keyMaterial,
        { name: "AES-CBC" },
        false,
        ["decrypt"]
    );
    // Crea un vector de inicialización (IV) de 16 bytes
    const iv = new Uint8Array(16);
    // Convierte el texto cifrado en formato base64 a un array de bytes
    const encryptedData = Uint8Array.from(atob(ciphertext), c => c.charCodeAt(0));
    // Realiza la operación de descifrado.
    const decrypted = await window.crypto.subtle.decrypt(
        {
            name: "AES-CBC",
            iv,
        },
        key,
        encryptedData
    );
    // Devuelve el texto descifrado.
    return decoder.decode(decrypted);
}

// Esta función realiza el cifrado de un texto utilizando el algoritmo AES-CBC
// con una clave generada mediante la función getKey(). Devuelve el texto cifrado en formato base64.
export async function encrypt(plaintext) {
    // Crea un codificador de texto
    const encoder = new TextEncoder();
    const keyMaterial = await getKey();
    // Importa la clave en el formato "raw" para cifrado AES-CBC
    const key = await window.crypto.subtle.importKey(
        "raw",
        keyMaterial,
        { name: "AES-CBC" },
        false,
        ["encrypt"]
    );
    // Crea un vector de inicialización (IV) de 16 bytes
    const iv = new Uint8Array(16);
    // Realiza la operación de cifrado utilizando AES-CBC y el IV
    const encrypted = await window.crypto.subtle.encrypt(
        {
            name: "AES-CBC",
            iv,
        },
        key,
        encoder.encode(plaintext)
    );
    // Convierte los bytes cifrados en formato base64 y los devuelve como una cadena
    return window.btoa(String.fromCharCode.apply(null, new Uint8Array(encrypted)));
}


