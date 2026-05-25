package at.lenz.termdesk;

import java.util.List;

public record AppMenuItem(
    String label,
    String icon,
    List<String> command,
    List<String> exit,
    StartMode mode,
    int row,
    int column) {}
