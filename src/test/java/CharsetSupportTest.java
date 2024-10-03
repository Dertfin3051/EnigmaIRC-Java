import ru.dfhub.eirc.Gui;

public class CharsetSupportTest {
    public static void main(String[] args) {
        Gui.init();
        Gui.showWelcomeMessage();
        Gui.showNewMessage("English text", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("Русский текст", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("English text", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("Unicode symbols: ☢ ☣ ☤ ☥ ☧ ☨ ☩ ☪ ☫ ☬ ☭ ☮ ☯ ☰ ☱ ☲ ☳ ☴ ☵ ☶ ☷ ♮ ♯ ♰ ♱ ✁ ✂ ✃ ✄ ✆ ✇ ✈ ✉ ✌ ✍ ✎ ✏ ✐ ✑ ✒ ✓ ☢ ☣ ☯ ☮ ☣", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("ANSI symbols: Æ Þ æ ð ÿ", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("Греческий алфавит: α ε η ι о υ ω β γ δ ζ θ κ λ μ ν ξ π ρ σ τ φ χ ψ", Gui.MessageType.USER_MESSAGE);
    }
}
