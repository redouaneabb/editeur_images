//role de cette partie:
//faire le lien avec fxml
//fais une action en finction de ce que l'utilisateur fais


package ProjetFinale;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    // on recupere dans le fxml
    @FXML private ImageView imageView;
    @FXML private ListView<String> listTags;
    @FXML private TextField nouveauTagField;
    @FXML private ComboBox<String> comboFiltres;

    // stockage temp
    private ImageModel modeleActuel;
    //ajout du DAO pour sauvegarder/charger
    private ImageDAO dao;

    private List<File> resultatSearch=new ArrayList<>();
    private int indexRes=0;

    @FXML private Button btnSuivant;

    @FXML
    protected void handleImport(){
        FileChooser chooser=new FileChooser();
        chooser.setTitle("Ouvrir une image");

        // permet de prendre que img dans ressources car chemin relatif

        File dossierInitial=new File("resources");
        if(dossierInitial.exists()){
            chooser.setInitialDirectory(dossierInitial);
        }
        //filtre pour voir que img
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));

        // bloque l'app jusqu'a choix de user
        File file = chooser.showOpenDialog(imageView.getScene().getWindow());

        if(file != null){
            //on crée le model
            modeleActuel = new ImageModel(file);
            //on set img dans vue
            //toURi... fais la traduction pour lire fichier
            imageView.setImage(new Image(file.toURI().toString(),false));

            // on vide tags vue pour nouvelle image
            listTags.getItems().clear();

            //essaye de charger une save
            tenterChargementAuto(file);

        }
    }
    //cette methode se lance automatiquement au chargement du FXML
    @FXML
    public void initialize(){
        dao=new ImageDAO();
        comboFiltres.getItems().addAll("Noir et Blanc","Sépia","Echange RGB","Prewitt","Rotation 90°","Miroir");
        if(btnSuivant!=null){
            btnSuivant.setVisible(false);
        }
    }
    //on aurait pu/du combiner handleChiffre et handleDechiffre en une seule methode
    @FXML
    protected void handleChiffre(){
        if(imageView.getImage()==null){
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sécurité");
        dialog.setHeaderText("Chiffrement de l'image");
        dialog.setContentText("Veuillez choisir un mot de passe");
        // on affiche et recupere directemennt chaine
        dialog.showAndWait();
        String mdp=dialog.getResult();

        if(mdp!=null && !mdp.isEmpty()){
            executerSecurite(mdp,true);
        }
    }
    @FXML
    protected void handleDechiffrer(){
        if(imageView.getImage()==null){
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dechiffrement");
        dialog.setHeaderText("Dechiffrement de l'image");
        dialog.setContentText("Veuillez entrer un mot de passe");

        dialog.showAndWait();
        String mdp=dialog.getResult();

        if(mdp!=null && !mdp.isEmpty()){
            executerSecurite(mdp,false);
        }
    }
    private void enregistrerImgDisque(Image image,File destination){
        try{
            int w=(int) image.getWidth();
            int h=(int) image.getHeight();
            BufferedImage bufImage=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
            int[] px=new int[w*h];

            image.getPixelReader().getPixels(0, 0, w, h, PixelFormat.getIntArgbPreInstance(), px, 0, w);
            bufImage.setRGB(0, 0, w, h, px, 0, w);

            String nom=destination.getName().toLowerCase();
            String format;
            if (nom.endsWith(".png")) {
                format = "png";
            } else {
                format = "jpg";
            }
            ImageIO.write(bufImage,format,destination);


        }
        catch(IOException e){
            System.err.println("Erreur d'ecriture sur le disque : "+e.getMessage());
        }
    }
    // IMPORTANT cette methode va permettre a une image de rester chiffrée meme quand on ferme l'app
    // Cepandant cela la rend extrement securise car au mondre erreur de mot de passe dans dechiffrer
    // l'image restera brouille a vie car on ecrase l'ancienne image a chaque chiffrement
    // les filtres esthetiques sont non destructif car stockés dans le json et réappliques donc l'original reste intact
    // le chiffrement est destructif par necessite de securite mais peu pratique
    private void executerSecurite(String mdp,boolean modeChiffrement){
        try{
            Image imgSource=imageView.getImage();
            if(imgSource==null){
                return;
            }
            int w=(int)imgSource.getWidth();
            int h=(int)imgSource.getHeight();
            //prepa tableau px
            int[] pixels=new int[w*h];
            imgSource.getPixelReader().getPixels(0,0,w,h, PixelFormat.getIntArgbInstance(),pixels,0,w);

            if(modeChiffrement){
            ServiceSecurite.melangerPixels(pixels,mdp);
            }
            else{
                ServiceSecurite.demelangerPixels(pixels,mdp);
            }

            WritableImage newImg=new WritableImage(w,h);
            newImg.getPixelWriter().setPixels(0,0,w,h,PixelFormat.getIntArgbInstance(),pixels,0,w);
            imageView.setImage(null);
            imageView.setImage(newImg);

            String nomAction;
            if(modeChiffrement){
                nomAction="Chiffrement";
            }
            else{
                nomAction="Dechiffrement";
            }
            if(modeleActuel!=null){
                modeleActuel.setImageActive(newImg);
                modeleActuel.setEstChiffre(modeChiffrement);
                modeleActuel.enregistrerTransfos(nomAction);
                enregistrerImgDisque(newImg,modeleActuel.getFichier());
                sauvegardeDonnees();
            }
        }
        catch(Exception e){
            System.err.println("Erreur de securite : "+e.getMessage());
        }
    }
    @FXML
    protected void handleTraitement(){
        String selection=comboFiltres.getValue();
        if(modeleActuel == null || selection== null){
            return;
        }

        //applique filtre
        appliquerFiltre(selection);

        //enregistre et sauvegarde
        modeleActuel.enregistrerTransfos(selection);
        sauvegardeDonnees();
    }

    @FXML
    protected void handleAjouterTag(){
        //recup ce que user tape ans boite de txt
        String texte=nouveauTagField.getText();

        if(!texte.isEmpty() && modeleActuel != null){
            //on ajoute dans les données
            modeleActuel.ajouterTag(texte);
            //on ajoute dans la liste
            listTags.getItems().add(texte);
            //on vide le champ texte
            nouveauTagField.clear();

            //on save dès que tag ajouté
            sauvegardeDonnees();

        }
    }

    private void sauvegardeDonnees(){
        try {
            //on cree un nom de fichier JSON base sur img
            String nomJson = "resources/" + modeleActuel.getFichier().getName() + ".json";
            dao.sauvegarder(modeleActuel, nomJson);
        }
        catch (IOException e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de sauvegarder : "+e.getMessage());
            alert.showAndWait();
        }
    }

    private void tenterChargementAuto(File imgfile){
        try {
            String nomJson = "resources/"+ imgfile.getName()+ ".json";
            File fJson=new File(nomJson);
            if(fJson.exists()){
                ImageModel charge=dao.charger(nomJson);
                //on restaure les tags
                this.modeleActuel.setTags(charge.getTags());
                listTags.getItems().setAll(charge.getTags());

                // restaure historique
                this.modeleActuel.setHistoriqueTransfos(charge.getHistoriqueTransfos());
                this.modeleActuel.setEstChiffre(charge.isEstChiffre());
                // si c'est chiffré on ne reaplique pas les filtres
                if(!charge.isEstChiffre()) {
                    for (String nomFiltre : charge.getHistoriqueTransfos()) {
                        appliquerFiltre(nomFiltre);
                    }
                }
                else{
                    System.out.println("image chiffrée detectée : filtre mis en attente du déchiffrement");
                }
                System.out.println("Sauvegarde chargée pour cette image");

            }
        }
        catch (IOException e){
            System.out.println("pas de sauvegarde trouve");
        }
    }

    private void appliquerFiltre(String nomFiltre){
        Image imgSrc=imageView.getImage();
        if(imgSrc==null){
            return;

        }
        Filtre filtreToDo;
        switch(nomFiltre){
            case "Noir et Blanc":
                filtreToDo = new FiltreGris();
                break;
            case "Sépia":
                filtreToDo = new FiltreSepia();
                break;
            case "Echange RGB":
                filtreToDo = new FiltreEchange();
                break;
            case "Prewitt":
                filtreToDo = new FiltrePrewitt();
                break;
            case "Rotation 90°":
                filtreToDo = new TransformationRotation();
                break;
            case "Miroir":
                filtreToDo = new TransformationSymetrie();
                break;
            default:
                return;
        }
        Image resultat=filtreToDo.appliquer(imgSrc);
        imageView.setImage(resultat);

        if(modeleActuel!=null){
            modeleActuel.setImageActive(resultat);
        }
    }

    @FXML protected void handleRecherche(){
        String tagVoulu=nouveauTagField.getText();
        if(tagVoulu.isEmpty()){
            return;
        }
        System.out.println("Recherche de l'image avec tag : "+tagVoulu);
        // on liste tout les fichiers du dossiers ressources
        resultatSearch.clear();
        indexRes=0;
        File dossier=new File("resources");
        File[] fichiersJson=dossier.listFiles();

        //parcours tous les fichiers du dossier
        if(fichiersJson!=null){
            for(File f : fichiersJson){
                if(f.getName().toLowerCase().endsWith(".json")){
                    try{
                        ImageModel temp=dao.charger(f.getAbsolutePath());

                        //verifie si ma liste des tags contient le mot
                        if(temp.getTags()!=null && temp.getTags().contains(tagVoulu)){
                            if(temp.getFichier()!=null && temp.getFichier().exists()) {
                                resultatSearch.add(temp.getFichier());
                            }
                        }
                    }
                    catch (IOException e){
                        System.err.println("Erreur lors de la fichier : "+f.getName());
                    }
                }
            }
        }
        if(!resultatSearch.isEmpty()){
            System.out.println(resultatSearch.size()+ " image trouvés");
            chargerImgSpe(resultatSearch.get(0)); // on affiche first
            btnSuivant.setVisible(resultatSearch.size()>1);
        }
        else {
            System.out.println("aucune img trouvé pour : "+tagVoulu);
            btnSuivant.setVisible(false);
        }



    }
    @FXML
    protected void handleSuivant(){
        if(resultatSearch.isEmpty()){
            return;
        }
        // on utilise le modulo pour boucler a la fin
        indexRes=(indexRes+1)%resultatSearch.size();
        File nextFichier=resultatSearch.get(indexRes);
        chargerImgSpe(nextFichier);

    }

    @FXML
    protected void handleReset(){
        if(modeleActuel==null){
            return;
        }
        File origine=modeleActuel.getFichier();
        imageView.setImage(new Image(origine.toURI().toString()));

        modeleActuel.setTags(new ArrayList<>());
        modeleActuel.setHistoriqueTransfos(new ArrayList<>());
        modeleActuel.setEstChiffre(false);

        listTags.getItems().clear();
        String nomJson ="resources/"+origine.getName()+".json";
        File fJson=new File(nomJson);
        if(fJson.exists()){
            fJson.delete();
        }

    }
    private void chargerImgSpe(File file){
        this.modeleActuel = new ImageModel(file);
        imageView.setImage(new Image(file.toURI().toString(),false));
        listTags.getItems().clear();
        tenterChargementAuto(file);
    }
}
