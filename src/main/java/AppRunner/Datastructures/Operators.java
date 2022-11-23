package AppRunner.Datastructures;

public enum Operators {
    NEW, READ, READTO, ADD, CHANGE, OPEN, RECOMMEND, SHOW, LIST, LISTALL;

    @Override
    public String toString() {
        return switch (this) {
            case NEW -> "New";
            case READ -> "Read";
            case READTO -> "Read-To";
            case ADD -> "Add";
            case CHANGE -> "Change";
            case OPEN -> "Open";
            case RECOMMEND -> "Recommend";
            case SHOW -> "Show";
            case LIST -> "List";
            case LISTALL -> "List-All";
        };
    }
}
