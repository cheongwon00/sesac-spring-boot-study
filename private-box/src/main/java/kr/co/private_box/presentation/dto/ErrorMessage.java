package kr.co.private_box.presentation.dto;

import java.util.List;

public class ErrorMessage {
    private List<String> errors;
    public ErrorMessage(List<String> errors){
        this.errors=errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
