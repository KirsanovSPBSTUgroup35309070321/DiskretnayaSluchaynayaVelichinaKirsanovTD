package DiskretnayaSluchaynayaVelichina;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    static long totalWords; // Всего слов в файле
    static Main.EntityFF Ent; // временная переменная для расчетов
    static double MathWait;   // Математическое ожидание
    static double Dispersion;   // Случайная дисперсия дисперсии

    static void PRINTRes(List<EntityFF> EntLIST, double MathWait, double Dispersion){
        // String.format("%1$"+maxLength+ "s", entry.getKey()) +": " + entry.getValue().toString());



        System.out.print("Строки:     ");

        for(int i=0; i<EntLIST.size(); i++)

            System.out.format("%12d", i);

        System.out.println();


        System.out.print("Xi:       ");


        for(EntityFF EFF : EntLIST)


            System.out.format("%12d", EFF.getXi());


        System.out.println();

        System.out.print("Pi:        ");

        for(EntityFF EFF : EntLIST)

            System.out.format("%12f", EFF.getPi());

        System.out.println();

        System.out.println("Математическое ожидание: "+ MathWait);
        System.out.println("Случайная дисперсия: "+ Dispersion);
    }


    public static void main(String[] args) throws IOException {


        System.out.println("Введите имя файла: ");

        System.out.println("* Имеется следующий выбор \"Tolstoy.txt\"?");

        try (final Stream<String> lines =
                     Files.lines(Paths.get("src/DiskretnayaSluchaynayaVelichina/" + new Scanner(System.in).nextLine()))

                             .map(line -> line.split("[-\\t,;.?!:@\\[\\](){}_*/\\s+]+")).flatMap(Arrays::stream)){

            //создание списка со всеми словами из файла


            List<String> StorLIST = lines.filter(x -> x.length() != 0).collect(Collectors.toList());

            // вычисление максимальной длины слова

            int MAXL = 0;
            OptionalInt MAXO  = StorLIST
                    .stream()
                    .mapToInt(String::length)
                    .max();
            MAXL = MAXO.orElse(-1);

            totalWords = StorLIST.size();

            // counting of number words of each length
            ArrayList<EntityFF> EntLIST = (ArrayList)CreateENTLIST(StorLIST, MAXL);

            // подсчет вероятности для каждой длины
            calculatePiMD( EntLIST);

            //результат печати

            PRINTRes(EntLIST, MathWait, Dispersion);

        } catch (NullPointerException npe) {
            System.out.println("NullPointerException");
        } catch (NoSuchFileException ex){
            System.out.println("Файла не существует");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    static  List<EntityFF> CreateENTLIST(List<String> StorLIST, int MAXL)
    {
        ArrayList<EntityFF> EntLIST = new ArrayList<EntityFF>(MAXL+1);

        for (int i=0; i<(MAXL+1); i++) {

            Ent = new Main().new EntityFF();

            final int CurL = i;

            Ent.setXi(StorLIST.stream().filter(x -> x.length() == CurL).count());

            EntLIST.add(CurL, Ent);
        }
        return EntLIST;
    }



    static void calculatePiMD(List<EntityFF> EntL) {
        for (int i = 1; i < (EntL.size()); i++) {
            Ent = EntL.get(i);
            Ent.setPi(((double) Ent.getXi()) / ((double) totalWords));
            MathWait += (double) Ent.getXi() * Ent.getPi();
            Dispersion += (double) Ent.getXi() * (double) Ent.getXi() * Ent.getPi();
            EntL.set(i, Ent);
        }
        Dispersion -= MathWait * MathWait;
    }


    class EntityFF {



        double Pi;  // Вероятность

        long Xi;  // частота




        public double getPi() {

            return Pi;
        }

        public void setPi(double pi) {

            Pi = pi;
        }

        public long getXi() {

            return Xi;
        }

        public void setXi(long xi) {

            Xi = xi;
        }
    }
}
