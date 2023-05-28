package start;


import model.Trial;


public class TestJava {
    private final static Client usersClient=new Client();
    public static void main(String[] args) {
        getAll();
        getOne();
        addOne();
        update();
        delete();

    }

    private static void getAll() {
        show(() -> {
            Trial[] res = usersClient.getAll();
            System.out.println("GET ALL:\n");
            for (Trial u : res) {
                System.out.println(u.getId() + ": " +u.getId()+" name:"+ u.getName());
            }
        });

    }
    private static void getOne() {
        show(()->{
            Trial res=usersClient.getOne(36);
            System.out.println("GET item with id 36:\n");
            System.out.println(res.getId()+": "+res.getName());
        });
    }

    private static void addOne() {
        show(()->{
            Trial id =usersClient.addTrial( new Trial("Poker",18,33));
            System.out.println(id);

        });
    }


    private static void update() {
        show(()->{
            Trial res=usersClient.getOneSpecific(new Trial("Poker",18,33));
            String updated=usersClient.update(new Trial( (long)res.getId(),"Poker start boy",18,33));
            System.out.println(updated);
        });
    }
    private static void delete() {
        show(()->{
            Trial trial=usersClient.getOneSpecific(new Trial("Poker start boy",18,33));
            String updated=usersClient.delete(trial.getId());
            System.out.println(updated);
        });
    }


    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Service exception"+ e);
        }
    }
}
