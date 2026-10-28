package ProjetFinale;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
// s'occupe du chiffrement dechiffrement
public class ServiceSecurite {
    // on transforme le texte en tableau via SHA-256
    private static byte[] hacherMotDePasse(String mdp) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(mdp.getBytes(StandardCharsets.UTF_8));
    }
    // Algo de Fisher-Yates
    public static void melangerPixels(int[] pixels,String mdp) throws Exception {
        byte[] seed=hacherMotDePasse(mdp);

        //SecureRandom initialise avec seed mdp
        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed);

        //on parcour tableau a l'envers pour melanger
        for(int i= pixels.length-1;i>0;i--){
            int j=random.nextInt(i+1);
            //echange px
            int temp=pixels[i];
            pixels[i]=pixels[j];
            pixels[j]=temp;
        }
    }

    //pour dechiffre faire inverse du melange
    public static void demelangerPixels(int[] pixels,String mdp) throws Exception {
        byte[] seed=hacherMotDePasse(mdp);

        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed);
        // on genere meme suite de nb mais on utilise a l'envers
        int[] positions =new int[pixels.length];
        for(int i= pixels.length-1;i>0;i--){
            positions[i]=random.nextInt(i+1);
        }
        for(int i=1;i<pixels.length;i++){
            int j=positions[i];
            int temp=pixels[i];
            pixels[i]=pixels[j];
            pixels[j]=temp;
        }
    }
}
