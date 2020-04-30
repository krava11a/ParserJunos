package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

//        ParserConfigJunos parserConfigJunos =
//                new ParserConfigJunos("/home/krava/IdeaProjects/ParserJunos/src/input/interfaces mx01.Stockholm");
        try (Stream<Path> paths = Files.walk(Paths.get("/home/krava/IdeaProjects/ParserJunos/src/input"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((x)-> {
                        try {
                            Router r = new Router(x.toString());
                            System.out.println(r.getNameRouter());
                            r.writeConfigToFileTxt();
                            r.getInterfaces().forEach((z) -> System.out.println(z.toString()));
                            //new ParserConfigJunos(x.toString()).parse().forEach((z) -> System.out.println(z.toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

//        parserConfigJunos.parse().forEach((x) -> System.out.println(x.toString()));
    }
}
