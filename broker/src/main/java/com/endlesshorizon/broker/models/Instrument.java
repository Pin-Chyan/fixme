package com.endlesshorizon.broker.models;

import lombok.Getter;
import lombok.Setter;

public class Instrument {
	private @Getter @Setter String type;
	private @Getter @Setter String instrument;
	private @Getter @Setter float price;
	private @Getter @Setter int quantity;
}
