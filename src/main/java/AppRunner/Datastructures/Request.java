package AppRunner.Datastructures;

public record Request(String command) {

    public static Request standard() {
        return new Request("");
    }
}
