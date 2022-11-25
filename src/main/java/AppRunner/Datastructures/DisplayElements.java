package AppRunner.Datastructures;

public record DisplayElements(boolean displayread, boolean displaylink, boolean displayrating, boolean displaylastread,
                              boolean displaypauseduntil, boolean displayreadingstatus, boolean displaywritingstatus) {
    // rework to List<Display>
    public String asCommand() {
        StringBuilder sb = new StringBuilder();
        if (displayread) sb.append("r");
        if (displaylink) sb.append("lk");
        if (displayrating) sb.append("rtg");
        if (displaylastread) sb.append("lr");
        if (displaypauseduntil) sb.append("pu");
        if (displayreadingstatus) sb.append("rs");
        if (displaywritingstatus) sb.append("ws");
        return sb.toString();
    }

}
