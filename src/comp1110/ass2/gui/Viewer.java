package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A very simple viewer for piece placements in the steps game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */

public class Viewer extends Application {

    /* board layout */
    public static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;
    //private static final int PIECE_IMAGE_SIZE = (int) ((3*SQUARE_SIZE)*1.33);
    private static int refershed = 0;
    private static final String URI_BASE = "assets/";

    /* node groups */
    private final Group root = new Group();
    private final Group pieces = new Group();
//    private final Group solution = new Group();
//    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group newPiece = new Group();
    private final Group pegs = new Group();

    TextField textField;

    /* message on completion */
    private final Text completionText = new Text("Well done!");

    private static final String[] Pieceset = {"AA","AE","BA","BE","CA","CE","DA","DE","EA","EE","FA","FE","GA","GE","HA","HE"};

    private static final String[] Pieceset_up = {"AA","AE","BA","BE","CA","CE","DA","DE"};

    private static final String[] Pieceset_left = {"EA","EE","FA","FE"};

    private static final String[] Pieceset_right = {"GA","GE","HA","HE"};

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */

    void makePlacement(String placement) {
        // FIXME Task 4: implement the simple placement viewer
//        if (!(placement.length()%3==0))
//            throw new IllegalArgumentException("Bad placement: " + placement + " ");
        int group = placement.length()/3;
        for (int i=0;i<group;i++){
            placePieces(placement.substring(3*i,3*i+3));
        }
    }

    public void placePieces (String piece){
        //System.out.println(piece);
        ImageView Piece = new ImageView();
        String char1 = String.valueOf(piece.charAt(0));
        String char2 = String.valueOf(piece.charAt(1));
        String location = String.valueOf(piece.charAt(2));
        int wid = 120;
        String imageName;
        String ImageChar2 = "ABCD";  // non-flipped
        // decode the board:
        final String[] Rotate = {"BF","CG","DH"}; //Rotate_90_180_270
        // Column 1-10 (index 0-9)
        final String[] Column = {"AKUfp","BLVgq","CMWhr","DNXis","EOYjt","FPaku","GQblv","HRcmw","ISdnx","JTeoy"};
        // Row 1-5 (index 0-4)
        final String[] Row = {"ABCDEFGHIJ","KLMNOPQRST","UVWXYabcde","fghijklmno","pqrstuvwxy"};
        // set the imageName (whether flipped)  imported from the folder
        if (ImageChar2.contains(char2))
            imageName = char1 + "A";
        else
            imageName = char1 + "E";
        Image name = new Image(Viewer.class.getResource("assets/"+imageName+".png").toString());
        Piece.setImage(name);
        Piece.setFitWidth(110);  //PIECE_IMAGE_SIZE
        Piece.setFitHeight(110);

        // rotate the pieces
        for (int i=0;i<3;i++){
            if(Rotate[i].contains(char2))
                Piece.setRotate(90*(i+1));
        }
        // locate the board
        Onepeg first_peg =new Onepeg( 1);

        //set the x-coordinate
        for (int i =0;i<10;i++){
            if(Column[i].contains(location))
                Piece.setX(first_peg.x+(i-1)*30-25);
                //System.out.println(first_peg.x+i*30+"xxxx");
        }
        //set the y-coordinate
        for (int i=0;i<5;i++){
            if(Row[i].contains(location))
                Piece.setY(first_peg.y+(i-1)*30-25);
                //System.out.println(first_peg.y+i*30+"yyyyy");
        }

        root.getChildren().add(Piece);
    }



    /**
     * Put all of the masks back in their home position
     */
    private void resetPieces(String placement) {
        for(int i=0;1<16;i++){
            newPiece.getChildren().add(new NewPiece(Pieceset[i],placement));
        }
    }


    class Picture extends ImageView{
        String piece;
        Picture(String piece){
            for(int i=0;1<16;i++){
                if (Pieceset[i] == piece){
                    setImage(new Image(Viewer.class.getResource(URI_BASE+piece+".png").toString()));
                    this.piece = piece;
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                    break;}
            }
//            throw new IllegalArgumentException("Bad "+piece);
        }
    }


    private static final Map<String,Integer> piecemap;
    static {
        piecemap = new HashMap<String,Integer>();
        piecemap.put("AA",50);    //    x  y= 150
        piecemap.put("AE",160);
        piecemap.put("BA",240);
        piecemap.put("BE",360);
        piecemap.put("CA",460);
        piecemap.put("CE",550);
        piecemap.put("DA",640);  // x 160 y == 135
        piecemap.put("DE",760);  // x 160 y == 215
        piecemap.put("EA",135); ////   y   x= 100
        piecemap.put("EE",220);
        piecemap.put("FA",280);
        piecemap.put("FE",350);
        piecemap.put("GA",130); // y 330 x 230
        piecemap.put("GE",210); // y 330 x 430
        piecemap.put("HA",135);
        piecemap.put("HE",270);
    }



    class NewPiece extends Picture{
        double homeX;
        double homeY;
        NewPiece(String piece,String placement){
            super(piece);
            if (piece=="DE"||piece=="DA"){
                homeX = 160;
                if (piece=="DA") {homeY = 125;}
                else homeY= 225;
            }
            else if (piece=="GA" || piece == "GE") {
                homeY=330;
                if (piece=="GA") {homeX =230;}
                else homeX= 430;
            }
            else if (Arrays.asList(Pieceset_up).contains(piece)){
                homeX = piecemap.get(piece);
                homeY = 20;}
            else if  (Arrays.asList(Pieceset_left).contains(piece)){
                homeY = piecemap.get(piece);
                homeX = 60;
            }
            else if  (Arrays.asList(Pieceset_right).contains(piece)){
                homeX = 560;
                homeY = piecemap.get(piece);
            }
            setLayoutX(homeX-30);
            setLayoutY(homeY );
            if (piece=="FA" || piece=="GA"||piece=="CE"||piece=="DA")
                setRotate(45);
            if (piece=="FE" || piece== "GE"||piece=="CA"||piece=="DE")
                setRotate(-45);
            if (piece=="EA")
                setRotate(90);
            if (piece=="EE")
                setRotate(-90);
            setFitHeight(100);
            setFitWidth(100);
        }
    }

    /**
     * Construct a particular peg at a particular place
     */
    class Onepeg extends Circle {
        double x,y;
        String num;
        Onepeg(int num){
            setRadius(10);
            if(num<=5) {
                setCenterX(250 + (num-1)* 60);    // 250 .. 20
                x = 250 + (num-1) * 60;   //      33
                setCenterY(200);
                y = 200;
            }
            else if(num<=10){
                setCenterX(280+(num-6)* 60);
                x = 280+(num-6)* 60;                   // 250 + 60
                setCenterY(200+30);
                y = 200+30;
            }
            else if(num<=15) {
                setCenterX(250 + (num - 11) * 60);
                x =250 + (num - 11) * 60;
                setCenterY(200+30*2);
                y =200+30*2;
            }
            else if(num<=20) {
                setCenterX(280 + (num-16) * 60);
                x = 280+(num-16)*60;
                setCenterY(200+30*3);
                y =200+30*3;
            }
            else if(num<=25) {
                setCenterX(250 + (num-21)*60);
                x = 250 + (num-21) * 60;
                setCenterY(200+30*4);
                y =200+30*4;
            }
            setStroke(Color.gray(0.6));
            setFill(Color.gray(0.6));
        }
    }

    private void makePegs() {
        pegs.getChildren().clear();
        for(int i = 1;i<=25;i++){
            pegs.getChildren().add(new Onepeg(i));
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(refershed==0){
                    makePlacement(textField.getText());
                    textField.clear();
                    refershed += 1;
                }
                else {
                    root.getChildren().clear();
                    root.getChildren().add(pegs);
                    root.getChildren().add(controls);
                    makePegs();
                    HBox hb = new HBox();
                    hb.getChildren().addAll(label1, textField, button);
                    hb.setSpacing(10);
                    hb.setLayoutX(130);
                    hb.setLayoutY(VIEWER_HEIGHT - 50);
                    controls.getChildren().add(hb);
                    makePlacement(textField.getText());
                    textField.setPrefWidth(300);
                    textField.clear();
                }
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
        }

    /**
     * The entry point for JavaFX.  This method gets called when JavaFX starts
     * The key setup is all done by this method.
     *
     * @param primaryStage The stage (window) in which the game occurs.
     * @throws Exception
     */

    @Override
    public  void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        root.getChildren().add(controls);
        root.getChildren().add(pieces);
        root.getChildren().add(newPiece);
//        root.getChildren().add(board);
//        root.getChildren().add(solution);
//        root.getChildren().add(controls);
        root.getChildren().add(pegs);
        makePegs();

        makeControls();


        primaryStage.setScene(scene);
        primaryStage.show();
        }
    }