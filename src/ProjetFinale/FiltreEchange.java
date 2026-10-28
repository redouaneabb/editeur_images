package ProjetFinale;

import javafx.scene.paint.Color;
//filtre qui fait une rotation des couleurs
public class FiltreEchange extends FiltreCouleur{
    @Override
    protected Color calculerCouleur(Color color){
        //on va juste changer l'ordre de l'ancienne couleure
        return new Color(color.getGreen(), color.getBlue(), color.getRed(), color.getOpacity());
    }


}
