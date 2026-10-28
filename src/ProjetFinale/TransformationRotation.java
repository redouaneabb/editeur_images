package ProjetFinale;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
// rotation 90° dans sens horaire
public class TransformationRotation implements Filtre{
    @Override
    public Image appliquer(Image imageEntree) {
        int largeur = (int) imageEntree.getWidth();
        int hauteur = (int) imageEntree.getHeight();
        //on inverse largeur et hauteur dans nouvelle image
        WritableImage sortie = new WritableImage(hauteur, largeur);
        PixelReader lecteur = imageEntree.getPixelReader();
        PixelWriter ecriture = sortie.getPixelWriter();

        for(int y=0;y<hauteur; y++) {
            for (int x=0;x<largeur; x++){
                // on a juste a inverser le pixel avec setArgb qui permet d'ecrire le pixel en entier
                // on utilise setArgb a la place de setColor car ca marchait moins bien ne sait pas pourquoi
                ecriture.setArgb(hauteur - 1 - y, x, lecteur.getArgb(x, y));
            }
        }
        return sortie;
    }
}
