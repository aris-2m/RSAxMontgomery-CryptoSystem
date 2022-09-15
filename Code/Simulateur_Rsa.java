package projet_cryptogr;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class InterfaceRsa extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField val_num;
	private JTextField val_p;
	private JTextField val_q;
	private JTextField val_e;
	private JTextField val_m;
	private JTextField val_resultat;
	private JTextField val_n2;
	private JTextField val_e3;
	private JTextField val_c;
	private JTextField val_d;
	private JTextField val_chiffre;

	/**
	 * Launch the application.
	 */	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					InterfaceRsa frame = new InterfaceRsa();
					frame.setLocationRelativeTo(null);
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


//L'algorithme d'euclide etendu pour trouver les inverse
	static int[] euclide_etendue1(int a,int b) {
		int x=1;int xx=0;int y=0;int yy=1;
		while(b!=0) {
			int q=a/b;
			int t1=a;
			a=b;
			b=t1%b;
			int t2=xx;
			xx=x-q*xx;
			x=t2;
			int t3=yy;
			yy=y-q*yy;
			y=t3;
		}
		int r1=a;
		int r2=x;
		int r3=y;
		return new int[] {r1,r2,r3};
	}
//La reduction de montgormery
	static int montgomery(int a,int b,int n) {
		int r=2;
		while(r<n) {
			r*=2;
		}
		int n1=euclide_etendue1(r,n)[2];
		if (n1<0) {
			n1=-n1;
		}
		else {
			n1=r-n1;
		}
		int s=a*b;
		int t=(s*n1)%r;
		int m=(s+t*n);
		int u=m/r;
		if (u>=n) {
			return u-n;
		}else {
			return u;
		}
	}
//puissance modulaire grace à montgomery
	static int puissance_modulaire(int a,int k,int n) {
		int r=2;
		while(r<n) {
			r*=2;
		}
		String N=Integer.toBinaryString(k);
		int m=(a*r)%n;
		int c=1;
		c=(c*r)%n;
		for(int i=0;i<N.length();i++) {
			c=montgomery(c,c,n);
			if(N.charAt(i)=='1') {
				c=montgomery(m,c,n);
			}
		}
		int p=montgomery(c,1,n);
		return p;
	}
//si un entier est premier
	 static boolean isPrime(long n) {
		    // fast even test.
		    if(n > 2 && (n & 1) == 0)
		       return false;
		    // only odd factors need to be tested up to n^0.5
		    for(int i = 3; i * i <= n; i += 2)
		        if (n % i == 0) 
		            return false;
		    return true;
		}
//codage rsa grace aux fonctions précedente
	 static int codage_rsa(int m,int p,int q,int e) {
		 int n=p*q;
		 return puissance_modulaire(m,e,n);
	 }
//trouver la clé privée
	 static int cle_privee(int p,int q,int e) {
			int phi=(p-1)*(q-1);
			
			int a=euclide_etendue1(e,phi)[1];
			int d=Math.floorMod(a, phi);
			return d; 
		 }
//decodage rsa
	 static int decodage_rsa(int x,int p,int q,int e) {
		 int n=p*q;
		 int d=cle_privee(p,q,e);
		 return puissance_modulaire(x,d,n);
	 }

	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
//codage rsa de chaine de caractere
	 static StringBuilder codage_chaine_rsa(String chaine,int p,int q,int e) {
			StringBuilder ascii=new StringBuilder();
			StringBuilder res=new StringBuilder();

			for(int i=0;i<chaine.length();i++) {
				if (Character.isLetter(chaine.charAt(i))){
					ascii.append((int)chaine.charAt(i));
				}
				else {
					ascii.append(chaine.charAt(i));
				}}
				for(int i=0;i<chaine.length();i++) {
					if (isNumeric(Integer.toString(ascii.charAt(i)))) {
						res.append(codage_rsa(chaine.charAt(i),p,q,e)+" ");
					}
					else {
					res.append(ascii.charAt(i));	
				}
			}
				return res;
		}
//decodage ave chaine de caractere
	 static StringBuilder decodage_chaine_rsa(String chaine,int p,int q,int e) {
			StringBuilder ascii=new StringBuilder();
			StringBuilder res=new StringBuilder();

			for(int i=0;i<chaine.length();i++) {
				if (Character.isLetter(chaine.charAt(i))){
					ascii.append(decodage_rsa((int)chaine.charAt(i),p,q,e));
				}
				else {
					ascii.append(decodage_rsa(chaine.charAt(i),p,q,e));
				}}
		for(int i=0;i<chaine.length();i++) {
		res.append((char)chaine.charAt(i));	
		
		}
		return res;
		}
	
	public InterfaceRsa() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1195, 747);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton quitter = new JButton("Quitter");
		quitter.setFont(new Font("Sitka Small", Font.BOLD, 20));
		quitter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
System.exit(DISPOSE_ON_CLOSE);
			}
		});
		quitter.setBounds(1012, 642, 133, 35);
		contentPane.add(quitter);
		
		JLabel lblC_1 = new JLabel("chiffr\u00E9");
		lblC_1.setForeground(Color.WHITE);
		lblC_1.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblC_1.setBounds(42, 497, 94, 48);
		contentPane.add(lblC_1);
		
		val_chiffre = new JTextField();
		val_chiffre.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_chiffre.setHorizontalAlignment(SwingConstants.CENTER);
		val_chiffre.setForeground(new Color(25, 25, 112));
		val_chiffre.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_chiffre.setColumns(10);
		val_chiffre.setBounds(167, 505, 317, 40);
		contentPane.add(val_chiffre);

		
		JButton btnNewButton_1_1 = new JButton("4.Envoi message chiffr\u00E9");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				val_chiffre.setText(val_c.getText());
			}
		});
		btnNewButton_1_1.setFont(new Font("Sitka Small", Font.BOLD, 20));
		btnNewButton_1_1.setBounds(811, 522, 296, 40);
		contentPane.add(btnNewButton_1_1);
		
		JLabel lblE_2 = new JLabel("e");
		lblE_2.setForeground(Color.WHITE);
		lblE_2.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblE_2.setBounds(966, 209, 45, 35);
		contentPane.add(lblE_2);
		
		JLabel alpha = new JLabel("Numeric");
		alpha.setForeground(Color.WHITE);
		alpha.setFont(new Font("Papyrus", Font.BOLD, 24));
		alpha.setBounds(693, 344, 101, 22);
		contentPane.add(alpha);
		
		val_num = new JTextField();
		val_num.setHorizontalAlignment(SwingConstants.CENTER);
		val_num.setForeground(new Color(25, 25, 112));
		val_num.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_num.setColumns(10);
		val_num.setBounds(811, 326, 205, 40);
		contentPane.add(val_num);
		
		JLabel lblAlice_1 = new JLabel("BOB");
		lblAlice_1.setForeground(Color.WHITE);
		lblAlice_1.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblAlice_1.setBounds(916, 10, 101, 40);
		contentPane.add(lblAlice_1);
		
		JLabel lblAlice = new JLabel("ALICE");
		lblAlice.setForeground(Color.WHITE);
		lblAlice.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblAlice.setBounds(197, 10, 161, 40);
		contentPane.add(lblAlice);
		
		JLabel lblNewLabel_1 = new JLabel("p");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblNewLabel_1.setBounds(93, 209, 45, 31);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("1.Envoi cl\u00E9 public");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int p=Integer.parseInt(val_p.getText());
				int q=Integer.parseInt(val_q.getText());
				int e1=Integer.parseInt(val_e.getText());
				int n=p*q;
				int phi=(p-1)*(q-1);
				if(e1>phi) {
					 UIManager.put("OptionPane.background",new ColorUIResource(39,50,70));
					 UIManager.put("Panel.background", new ColorUIResource(39,50,70));

					 JLabel message=new JLabel(e1+" doit etre inferieur à "+phi);
					 message.setFont(new Font("Sitka Small", Font.BOLD, 25));
					 message.setForeground(Color.WHITE);
					 JOptionPane.showMessageDialog( null, message);
					
				}if(!isPrime(p) || !isPrime(q)) {
					 UIManager.put("OptionPane.background",new ColorUIResource(39,50,70));
				     UIManager.put("Panel.background", new ColorUIResource(39,50,70));

				     JLabel message=new JLabel(p+" et  "+q+" doivent etre des nombres premiers");
				     message.setFont(new Font("Sitka Small", Font.BOLD, 25));
				     message.setForeground(Color.WHITE);
				     JOptionPane.showMessageDialog( null, message);
					
				}
				int pgcd=euclide_etendue1(e1,phi)[0];
				if (pgcd!=1) {
					 UIManager.put("OptionPane.background",new ColorUIResource(39,50,70));
					 UIManager.put("Panel.background", new ColorUIResource(39,50,70));

					 JLabel message=new JLabel("choisissez un 'e' premier avec "+" " +phi);
					 message.setFont(new Font("Sitka Small", Font.BOLD, 25));
					 message.setForeground(Color.WHITE);
					 JOptionPane.showMessageDialog( null, message);		
				}
				val_n2.setText(String.valueOf(n));
				val_e3.setText(String.valueOf(e1));
				val_n2.setText(String.valueOf(n));	
			}
		});
		btnNewButton.setFont(new Font("Sitka Small", Font.BOLD, 20));
		btnNewButton.setBounds(105, 358, 282, 38);
		contentPane.add(btnNewButton);
		
		JLabel lblQ = new JLabel("q");
		lblQ.setForeground(Color.WHITE);
		lblQ.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblQ.setBounds(93, 252, 45, 40);
		contentPane.add(lblQ);
		
		JLabel lblE = new JLabel("e");
		lblE.setForeground(Color.WHITE);
		lblE.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblE.setBounds(93, 307, 45, 35);
		contentPane.add(lblE);
		
		val_p = new JTextField();
		val_p.setHorizontalAlignment(SwingConstants.CENTER);
		val_p.setForeground(new Color(25, 25, 112));
		val_p.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_p.setColumns(10);
		val_p.setBounds(166, 208, 174, 40);
		contentPane.add(val_p);
		
		val_q = new JTextField();
		val_q.setForeground(new Color(25, 25, 112));
		val_q.setBackground(new Color(255, 255, 255));
		val_q.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_q.setHorizontalAlignment(SwingConstants.CENTER);
		val_q.setColumns(10);
		val_q.setBounds(166, 256, 174, 40);
		contentPane.add(val_q);
		
		val_e = new JTextField();
		val_e.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_e.setHorizontalAlignment(SwingConstants.CENTER);
		val_e.setForeground(new Color(25, 25, 112));
		val_e.setColumns(10);
		val_e.setBounds(166, 308, 174, 40);
		contentPane.add(val_e);
		
		JLabel lblM = new JLabel("Alpha");
		lblM.setForeground(Color.WHITE);
		lblM.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblM.setBounds(693, 266, 86, 35);
		contentPane.add(lblM);
		
		val_m = new JTextField();
		val_m.setHorizontalAlignment(SwingConstants.CENTER);
		val_m.setForeground(new Color(25, 25, 112));
		val_m.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_m.setColumns(10);
		val_m.setBounds(811, 267, 334, 40);
		contentPane.add(val_m);
		
		JButton btnChiffrer = new JButton("3.Chiffrer");
		btnChiffrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int p=Integer.parseInt(val_p.getText());
				int q=Integer.parseInt(val_q.getText());
				int e1=Integer.parseInt(val_e.getText());
				String message=val_m.getText();
				val_c.setText(codage_chaine_rsa(message,p,q,e1).toString());
		
				if(val_num.getText().length()!=0) {
					int M=Integer.parseInt(val_num.getText());
					val_c.setText(String.valueOf(codage_rsa(M,p,q,e1)));
				}
			}
		});
		btnChiffrer.setFont(new Font("Sitka Small", Font.BOLD, 20));
		btnChiffrer.setBounds(811, 394, 296, 40);
		contentPane.add(btnChiffrer);
		
		JLabel val_res = new JLabel("message");
		val_res.setForeground(Color.WHITE);
		val_res.setFont(new Font("Papyrus", Font.BOLD, 25));
		val_res.setBounds(42, 608, 109, 35);
		contentPane.add(val_res);
		
		val_resultat = new JTextField();
		val_resultat.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_resultat.setHorizontalAlignment(SwingConstants.CENTER);
		val_resultat.setForeground(new Color(25, 25, 112));
		val_resultat.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_resultat.setColumns(10);
		val_resultat.setBounds(167, 609, 317, 40);
		contentPane.add(val_resultat);
		
		JLabel lblne_2 = new JLabel("n");
		lblne_2.setForeground(Color.WHITE);
		lblne_2.setFont(new Font("Papyrus", Font.PLAIN, 25));
		lblne_2.setBounds(849, 212, 54, 28);
		contentPane.add(lblne_2);
		
		val_n2 = new JTextField();
		val_n2.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_n2.setHorizontalAlignment(SwingConstants.CENTER);
		val_n2.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_n2.setForeground(new Color(25, 25, 112));
		val_n2.setColumns(10);
		val_n2.setBounds(875, 210, 81, 40);
		contentPane.add(val_n2);
		
		val_e3 = new JTextField();
		val_e3.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_e3.setHorizontalAlignment(SwingConstants.CENTER);
		val_e3.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_e3.setForeground(new Color(25, 25, 112));
		val_e3.setColumns(10);
		val_e3.setBounds(992, 210, 81, 40);
		contentPane.add(val_e3);
		
		JButton btnDechiffrer = new JButton("5.Dechiffrer");
		btnDechiffrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int p=Integer.parseInt(val_p.getText());
				int q=Integer.parseInt(val_q.getText());
				int e1=Integer.parseInt(val_e.getText());
				String message=val_m.getText();
				val_resultat.setText(decodage_chaine_rsa(message,p,q,e1).toString());
				
				if(val_num.getText().length()!=0) {
					int k=(decodage_rsa(Integer.parseInt(val_chiffre.getText()), p, q, e1));
					val_resultat.setText(String.valueOf(k));
					
				}
			}
		});
		btnDechiffrer.setFont(new Font("Sitka Small", Font.BOLD, 20));
		btnDechiffrer.setBounds(105, 558, 282, 40);
		contentPane.add(btnDechiffrer);
		
		JButton btnNewButton_1 = new JButton("2.Generer cle priv\u00E9e");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int p=Integer.parseInt(val_p.getText());
				int q=Integer.parseInt(val_q.getText());
				int e1=Integer.parseInt(val_e.getText());
				val_d.setText(String.valueOf(cle_privee(p,q,e1)));	
			}
		});
		btnNewButton_1.setFont(new Font("Sitka Small", Font.BOLD, 20));
		btnNewButton_1.setBounds(105, 405, 282, 40);
		contentPane.add(btnNewButton_1);
		
		JLabel lblC = new JLabel("chiffr\u00E9");
		lblC.setForeground(Color.WHITE);
		lblC.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblC.setBounds(693, 451, 94, 48);
		contentPane.add(lblC);
		
		val_c = new JTextField();
		val_c.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_c.setHorizontalAlignment(SwingConstants.CENTER);
		val_c.setForeground(new Color(25, 25, 112));
		val_c.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_c.setColumns(10);
		val_c.setBounds(808, 459, 337, 40);
		contentPane.add(val_c);
		
		JLabel lblE_1 = new JLabel("d");
		lblE_1.setForeground(Color.WHITE);
		lblE_1.setFont(new Font("Papyrus", Font.BOLD, 25));
		lblE_1.setBounds(93, 455, 45, 40);
		contentPane.add(lblE_1);
		
		val_d = new JTextField();
		val_d.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(128, 0, 0)));
		val_d.setHorizontalAlignment(SwingConstants.CENTER);
		val_d.setFont(new Font("Perpetua Titling MT", Font.BOLD, 25));
		val_d.setForeground(new Color(25, 25, 112));
		val_d.setColumns(10);
		val_d.setBounds(167, 455, 86, 40);
		contentPane.add(val_d);
		
		JLabel lblNewLabel_1_1 = new JLabel("New label");
		lblNewLabel_1_1.setIcon(new ImageIcon("C:\\Users\\Lenovo\\Downloads\\user_female - Copie.png"));
		lblNewLabel_1_1.setBounds(160, 60, 198, 100);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("New label");
		lblNewLabel_1_1_1.setIcon(new ImageIcon("C:\\Users\\Lenovo\\Downloads\\53788 - Copie.png"));
		lblNewLabel_1_1_1.setBounds(872, 60, 198, 100);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Lenovo\\Downloads\\Capture - Copie.JPG"));
		lblNewLabel.setBounds(0, 0, 1187, 715);
		contentPane.add(lblNewLabel);
	}
}
