package at.lenz.termdesk;

import java.util.List;

public record AppMenuItem(String label, String icon, List<String> command, StartMode mode) {}
