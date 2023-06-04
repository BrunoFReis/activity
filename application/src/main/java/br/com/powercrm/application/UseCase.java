package br.com.powercrm.application;

import br.com.powercrm.domain.activity.Activity;

import java.text.ParseException;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn) throws ParseException;
}