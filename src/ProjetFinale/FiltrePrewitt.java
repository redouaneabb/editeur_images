package ProjetFinale;


import javafx.scene.image.*;
import javafx.scene.paint.Color;
// on fait le filtre prewitt
// ici on ne peut pas heriter de FiltreCouleur car cette classe ne nous donne
//acces que a la couleur d'un pixel nous on veut aussi les pixels autour
public class FiltrePrewitt implements Filtre{
    @Override
    public Image appliquer(Image imageEntree){
        int largeur=(int) imageEntree.getWidth();
        int hauteur=(int) imageEntree.getHeight();
        WritableImage sortie = new WritableImage(largeur,hauteur);
        PixelReader lecteur = imageEntree.getPixelReader();
        PixelWriter ecriture=sortie.getPixelWriter();

        // on evite les bords de 1 pour les marges pour ps sortir
        for(int y=1;y<hauteur-1;y++){
            for(int x=1;x<largeur-1;x++){

                //gradiant horizon
                double gx=(val(lecteur,x+1,y-1)+val(lecteur,x+1,y)+val(lecteur,x+1,y+1)-(val(lecteur,x-1,y-1)+val(lecteur,x-1,y)+val(lecteur,x-1,y+1)));
               //gradiant vert
                double gy=(val(lecteur,x-1,y+1)+val(lecteur,x,y+1)+val(lecteur,x+1,y+1)-(val(lecteur,x-1,y-1)+val(lecteur,x,y-1)+val(lecteur,x+1,y-1)));
                //amplitude du gradiant
                double gamp=Math.sqrt(gx*gx+gy*gy);

                gamp=Math.min(1.0,gamp);

                ecriture.setColor(x,y,new Color(gamp,gamp,gamp,1.0));
            }
        }
        return sortie;
    }
    //trouver val gris d'un pixel peut probablement etre optimiser plus tard avec flitre gris d'une certain facon
    private double val(PixelReader lecteur,int x,int y){
        Color coul=lecteur.getColor(x,y);
        return (coul.getRed()+coul.getGreen()+coul.getBlue())/3;
    }

}
