##L'algorithme d'euclide etendu pour trouver les inverses
import math
def euclid_etendu1(a,b):
    x = 1 ; xx = 0
    y = 0 ; yy = 1
    while b !=0 :
        q = a // b
        a , b = b , a % b
        xx , x = x - q*xx , xx
        yy , y = y - q*yy , yy
    return (a,x,y)
            

#La réduction de matgormery
def motgomery(a,b,n):
    r = 2
    while r < n:
        r *=2
    #n_1 = euclid_etendu(r,n)[1]
    n_1 = euclid_etendu1(r,n)[2]
    if n_1 < 0:
        n_1 =-n_1
    else:
        n_1 = r-n_1
    s = a*b
    t = (s*n_1)%r
    m = (s + t*n)
    u = m//r
    if u >= n:
        return u-n
    else:
        return u



def puissance_modulaire(a,k,n):
    r =2
    while r < n:
         r *=2
    N = bin(k)[2:]
    m = (a*r)%n
    c = 1
    c = (c*r)%n
    k = len(N)
    for i in range(k):
        c = motgomery(c,c,n)
        if N[i] == '1':
            c = motgomery(m,c,n) 
    p = motgomery(c,1,n)
    return p



# une fonction pour vérfier qu'un nombre est premier
def is_prime(n):
    if (n <= 1):
        return False
    for i in range(2, int(math.sqrt(n))+1):
        if (n % i == 0):
            return False
    return True

#La clé privée pour rsa
def cle_privee(p,q,e):
    phi = (p-1)*(q-1)
    if e > phi:
        raise Exception(f' La valeur doit etre inferieur a {phi}')
    if not is_prime(p) or not is_prime(q):
        raise Exception('p et q doit etre de nombre premier')
    pgcd = euclid_etendu1(e,phi)[0]
    if pgcd !=1:
        raise Exception(f'e doit etre premier avec {phi}')
    d= euclid_etendu1(e,phi)[1]
    return d%phi

def codage_rsa(m,p,q,e):
    n = p*q
    return puissance_modulaire(m,e,n)
def decodage_rsa(x,p,q,e):
    n = p*q
    d = cle_privee(p,q,e)
    return puissance_modulaire(x,d,n)


## pour coder de chaine de chractère

def codege_chaine_rsa(chaine,p,q,e):
    res = []
    acci = []
    #transformation en ascci
    for i in range(len(chaine)):
        acci.append(ord(chaine[i])) ## trouver la valeur de lettre corespondate en ascci
    #codage avec le rsa
    for i in range(len(acci)):
            res.append(codage_rsa(acci[i],p,q,e))
    return res

def decodage_chaine_rsa(arr,p,q,e):
    res = []
    for i in range(len(arr)):
        res.append(chr(decodage_rsa(arr[i],p,q,e)))
    return ''.join(res)

print(codege_chaine_rsa('',13,11,7))
print(decodage_chaine_rsa(codege_chaine_rsa('AMADOU! je suis moi>',13,11,7),13,11,7))
