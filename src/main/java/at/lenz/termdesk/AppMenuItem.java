package at.lenz.termdesk;

import java.util.List;

public record AppMenuItem(String label, List<String> command, StartMode mode) {}
