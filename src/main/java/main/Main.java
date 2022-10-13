package main;

import JavaFX.Controller;
import algorithm.AStarUtil;
import algorithm.ParallelAStar;
import algorithm.DFSParallel;
import io.InputLoader;
import io.OutputFormatter;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.TheGraph;
import org.graphstream.graph.Node;
import algorithm.AStar;
import models.Digraph;
import algorithm.PartialSolution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import utils.GraphGenerator;

import java.io.IOException;
import java.util.Collection;

public class Main extends Application {

    private static boolean visualise = true;
    private static PartialSolution solution = null;

    private static String INPUT_FILE = "input.dot";
    private static String OUTPUT_FILE = "input-output.dot";
    private static String numOfProcessors = "numOfProcessors";
    private static String numOfCore = "1";
    private static String currentBestTime = "UNKNOWN";
    private static String numOfTasks;
    private static Boolean isRunning = true;

    public static void main(String[] args) throws IOException {


        String path = args[0];
        INPUT_FILE = path;
        int processAmount = Integer.parseInt(args[1]);
        numOfProcessors = args[1];
        long startTime = System.currentTimeMillis();

//        if (args.length < 2) {
//            System.err.println("Invalid arguments");
//            return;
//        }
//
//        String path = args[0];
//        int processAmount = Integer.parseInt(args[1]);
//
//        InputLoader.setNumOfProcessors(processAmount);
//        InputLoader.loadDotFileFromPath(path);
//
//        AStar aStar = new AStar();
//        PartialSolution solution = aStar.buildTree();
//
//        OutputFormatter outputFormatter = new OutputFormatter();
//        outputFormatter.aStar(solution, path.substring(0, path.length() - 4));

        numOfTasks= String.valueOf(TheGraph.get().getNodeCount());

        if (visualise) {
            launch(args);
        }


        AStar aStar = new AStar();
        solution = aStar.buildTree(new PartialSolution());
//        Digraph inputGraph = InputLoader.loadDotFileFromPath("examples/g11/in.dot");
//        InputLoader.setNumOfProcessors(4);
//        ParallelAStar parallelAStar = new ParallelAStar(4);
//        System.out.println(parallelAStar.build().getInfo());


//        GraphGenerator.generateRandomGraphs(10);

//       if (args.length < 2) {
//           System.err.println("Invalid arguments");
//            return;
//        }


//        DFSParallel dfsParallel = new DFSParallel(4);
//        dfsParallel.findBestPartial();
//        System.out.println(dfsParallel.getBestPartialSolution().getInfo());


//        AStarUtil aStarUtil = new AStarUtil();
//        System.out.println(aStarUtil.getBestPartialSolution().getInfo());
//        ParallelAStar parallelAStar = new ParallelAStar(2);

//        PartialSolution build = parallelAStar.build();

//        System.out.println(build);

//        System.out.println(build.getInfo());
//
//
////        InputLoader.print(digraph, false);
////        BruteForce solutionTree = BruteForce.getSolutionTree(digraph, 1);
////        System.out.println(solutionTree.getNodeCount());
//
////        System.out.println("\n=== Final print ===");
////        System.out.println(solutionTree.getAllNodes().size());
////        InputLoader.print(solutionTree, false);
//
////        System.out.println(digraph.getAllNodes());
//
////        System.out.println(digraph.getBottomLevel(digraph.getNodeByValue("h")));
//
////        System.out.println(myGraph.getCriticalPath());
//
////
////        BruteForce solutionTree = new BruteForce(2);
////        solutionTree.build(solutionTree.getRoot());
////        solutionTree.print();
//
//
//
//        AStar aStar = new AStar();
//        PartialSolution p = aStar.buildTree(new PartialSolution());
//        System.out.println(p.getInfo());
//        new OutputFormatter().aStar(p, inputGraph);

//        System.out.println(p.calculateEndScheduleTime());
//        PartialSolution p = aStar.getLastPartialSolution();
//        System.out.println(p);

        long endTime = System.currentTimeMillis(); //获取结束时间

        System.out.println("time used：" + (endTime - startTime)/1000 + "s");
    }


    private static int countLeaves(Digraph digraph) {
        int count = 0;
        for (Node node : digraph.getAllNodes()) {
            if (digraph.getOutDegreeOfNode(node) == 0) {
                count++;
            }
        }
        return count;
    }

    private static void runAStar() {
        AStar aStar = new AStar();
        solution = aStar.buildTree(new PartialSolution());
        OutputFormatter outputFormatter = new OutputFormatter();
        outputFormatter.aStar(solution, INPUT_FILE.substring(0, INPUT_FILE.length() - 4));
    }

    public static PartialSolution getCurrentPartialSolution() {
        return solution;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/Visualization.fxml"));
        Controller controller= loader.getController();
        Parent root = loader.load();

        // run the algorithm on another thread
        new Thread(Main::runAStar).start();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/ganttChart.css").toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        stage.setResizable(false);
        stage.setTitle("Visualisation");
        stage.show();
    }


    public static String getInputFile() {return INPUT_FILE;}
    public static String getOutputFile() {return OUTPUT_FILE;}
    public static String getNumOfProcessors() {return numOfProcessors;}
    public static String getNumOfCore() {return numOfCore;}
    public static String getNumOfTasks() {return numOfTasks;}
    public static Boolean getIsRunning() {return isRunning;}
    public static String getCurrentBestTime() {
        if (solution == null) {
            return "CALCULATING";
        } else {
            isRunning = false;
            return String.valueOf(solution.calculateEndScheduleTime());
        }
    }
//    public PartialSolution getSolution() {return solution;}
}
