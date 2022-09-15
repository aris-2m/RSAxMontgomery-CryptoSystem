#Le chiffrement de hill
import numpy as np ## on utilise numpy pour le calcul de determinant et l'inverse de la matrice
from egcd import egcd   # une fonction pour calculer l'euclid etendu
import math
M = [[3,5],[6,17]] #une matrice 
m = "TEXTACRYPTERer"        # une chaine de caracteres pour faire un exemple
def chiffrement_hill(M,m):
    #M c la matrice et m le message a chifrrer
    # on travail dans Z/26Z
    messageVector =[[0]*len(M) for i in range(len(M))]
    if len(m)%len(M):
        raise Exception(f"La Longeur du message doit etre divisble par {len(M)}")
    arrVect = []
    if len(M) != len(M[0]):
        raise Exception("M doit etre une matrice carrer")
    det = int(np.linalg.det(M))
    #verifier si la matrice est inversible
    if math.gcd(det,26) !=1:
        raise Exception("La matrice doit etre inversible dans Z/26Z")
    # transformer le message en vecteurs
    for i in range(len(m)):
            arrVect.append(ord(m[i].upper())%65)
    messageVector = [[0]*len(M) for _ in range(len(m)//len(M))]
    for i in range(len(m)//len(M)):
        for x in range(len(M)):
            messageVector[i][x] = arrVect.pop(0)
    #Coder le message
    textchiffrer = []
    for vect in messageVector:
        chifrrer = np.dot(np.array(M),np.array(vect).T)
        chifrrer = [num%26 for num in chifrrer]
        textchiffrer.append(chifrrer)
        
    # transormer les entier en characteres
    trans = [chr(num + 65) for num in np.array(textchiffrer).flatten()]
    #faire un join epuis retourner la chaine chifrer
    return ''.join(trans)


#une fonction qui retourne l'inverse de la matrice modulo 26
def matrix_modulo_inverse(mat):
    #Le determinant
    det = int(np.round(np.linalg.det(mat)))
    det_inv = egcd(det,26)[1]%26
    mat_inver = det_inv * np.round(det*np.linalg.inv(mat)).astype(int)%26
    return mat_inver

#La fonction de dechiffrement
def dechiffrement_hill(M,c):
    arrVect = []
    for i in range(len(c)):
            arrVect.append(ord(c[i])%65)
    messageVector = [[0]*len(M) for _ in range(len(c)//len(M))]
    for i in range(len(c)//len(M)):
        for x in range(len(M)):
            messageVector[i][x] = arrVect.pop(0)
   
    M_inverse = matrix_modulo_inverse(M)
    textchiffrer = []
    for vect in messageVector:
        chifrrer = np.dot(M_inverse,np.array(vect).T)
        chifrrer = [num%26 for num in chifrrer]
        textchiffrer.append(chifrrer)
    trans = [chr(int(num) + 65) for num in np.array(textchiffrer).flatten()]
    #faire un join epuis retourner la chaine chifrer
    return ''.join(trans)
    
print(f'chiffrer de {m}  = {chiffrement_hill(M, m)}')
print(f'dechiffrer de {chiffrement_hill(M, m)} = {dechiffrement_hill(M,chiffrement_hill(M, m))}')
#print(matrix_modulo_inverse(M))