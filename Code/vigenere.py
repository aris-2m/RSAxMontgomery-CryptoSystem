#!/usr/bin/env python
# coding: utf-8

# In[18]:


alphabet = "abcdefghijklmnopqrstuvwxyz "

lettre_en_chiffre = dict(zip(alphabet, range(len(alphabet))))
chiffre_en_lettre = dict(zip(range(len(alphabet)), alphabet))


def chiffrement(message, key):
    code = ""
    ##separation du message en bloc correspondant a la taille de la clé
    bloc = [
        message[i : i + len(key)] for i in range(0, len(message), len(key))
    ]
    ##parcourir chaque bloc et additionner chaque lettre mis en chiffre à la clé correspondante
    for bl in bloc:
        i = 0
        for lettre in bl:
            nombre = (lettre_en_chiffre[lettre.lower()] + lettre_en_chiffre[key[i].lower()]) % len(alphabet)
            code += chiffre_en_lettre[nombre]
            i += 1

    return code


def dechiffrement(bloc_ch, key):
    dechiffr = ""
    ##bloc
    bloc_code = [
        bloc_ch[i : i + len(key)] for i in range(0, len(bloc_ch), len(key))
    ]
    ##parcourir bloc comme pour le chiffrement mais soutraire cette fois ci
    for bl in bloc_code:
        i = 0
        for lettre in bl:
            nombre = (lettre_en_chiffre[lettre] - lettre_en_chiffre[key[i].lower()]) % len(alphabet)
            dechiffr += chiffre_en_lettre[nombre]
            i += 1

    return dechiffr


def main():
    message = "nous avons projet de crypto"
    key = "ifzarne"
    code_message = chiffrement(message, key)
    dechiffr_message = dechiffrement(code_message, key)

    print("message original: " + message)
    print("chiffrement: " + code_message)
    print("Dechiffrement message: " + dechiffr_message)


main()


# In[ ]:





# In[ ]:




