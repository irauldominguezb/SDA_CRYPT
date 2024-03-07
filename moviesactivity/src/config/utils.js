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


export async function decrypt(ciphertext) {
    const decoder = new TextDecoder();
    const keyMaterial = await getKey(); 
    const key = await window.crypto.subtle.importKey(
        "raw",
        keyMaterial,
        { name: "AES-CBC" },
        false,
        ["decrypt"]
    );
    const iv = new Uint8Array(16);
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


export async function encrypt(plaintext) {
    const encoder = new TextEncoder();
    const keyMaterial = await getKey();
    const key = await window.crypto.subtle.importKey(
        "raw",
        keyMaterial,
        { name: "AES-CBC" },
        false,
        ["encrypt"]
    );
    const iv = new Uint8Array(16);
    const encrypted = await window.crypto.subtle.encrypt(
        {
            name: "AES-CBC",
            iv,
        },
        key,
        encoder.encode(plaintext)
    );
    return window.btoa(String.fromCharCode.apply(null, new Uint8Array(encrypted)));
}


