package ProjetFinale;

import javafx.scene.image.Image;
// contrat pour tout les filtre et transformations d'image
public interface Filtre {
    Image appliquer(Image imageEntree);
}
