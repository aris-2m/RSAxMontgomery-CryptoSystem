def masque(m,key):
    if len(key) < len(m):
        raise Exception('La clee doit etre superieur ou egal au message m')
    chaine_en_ascci =[ord(m[i].upper()) for i in range(len(m))]
    cle_en_ascci = [ord(key[i].upper()) for i in range(len(key))]
    text_chiffre = [chr((chaine_en_ascci[i] + cle_en_ascci[i])%26 +65) for i in range(len(m))]
    return ''.join(text_chiffre)


def dechifrer_masque(c,key):
    if len(key) < len(c):
        raise Exception('La longueur de la cle doit etre superieur ou egal a la longueur du message m')
    chaine_en_ascci =[ord(c[i].upper()) for i in range(len(c))]
    cle_en_ascci = [ord(key[i].upper()) for i in range(len(key))]
    text_chiffre = [chr((chaine_en_ascci[i] - cle_en_ascci[i])%26 +65) for i in range(len(c))]
    return ''.join(text_chiffre)

print("Chiffrement: "+masque("HELLOtut","WMCKLtut"))
print("Déchiffrement: "+ dechifrer_masque("DQNVZMOM","WMCKLtut"))
