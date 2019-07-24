package io.openems.edge.bridge.modbus.sunspec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;

import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.AbstractModbusElement;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedDoublewordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.bridge.modbus.api.task.Task;
import io.openems.edge.bridge.modbus.sunspec.SunSpecModelUtils.Point;
import io.openems.edge.bridge.modbus.sunspec.SunSpecModelUtils.SunSChannelId;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.taskmanager.Priority;

/**
 * This class provides a generic implementation of SunSpec ModBus protocols.
 */
public abstract class AbstractOpenemsSunSpecComponent extends AbstractOpenemsModbusComponent {

	private final ModbusProtocol modbusProtocol;

	protected AbstractOpenemsSunSpecComponent(io.openems.edge.common.channel.ChannelId[] firstInitialChannelIds,
			io.openems.edge.common.channel.ChannelId[][] furtherInitialChannelIds) {
		super(firstInitialChannelIds, furtherInitialChannelIds);
		this.modbusProtocol = new ModbusProtocol(this);
	}

	@Override
	protected boolean activate(ComponentContext context, String id, String alias, boolean enabled, int unitId,
			ConfigurationAdmin cm, String modbusReference, String modbusId) {
		// Start the SunSpec read procedure...
		this.isSunSpec().thenAccept(isSunSpec -> {
			System.out.println("Is SunSpec? " + isSunSpec);
			this.readNextBlock(40_002).thenRun(() -> {

				// TODO funktioniert noch nicht.

				System.out.println("#");
				System.out.println("# Finished parsing SunSpec");
				System.out.println("#");
			});
		});
		return super.activate(context, id, alias, enabled, unitId, cm, modbusReference, modbusId);
	}

	@Override
	protected final ModbusProtocol defineModbusProtocol() {
		return this.modbusProtocol;
	}

	/**
	 * Validates that this device complies to SunSpec specification.
	 * 
	 * <p>
	 * Tests if first registers are 0x53756e53 ("SunS").
	 * 
	 * @return a future true if it is SunSpec; otherwise false
	 */
	private CompletableFuture<Boolean> isSunSpec() {
		final CompletableFuture<Boolean> result = new CompletableFuture<Boolean>();
		this.readELementOnce(new UnsignedDoublewordElement(40_000)).thenAccept(value -> {
			if (value == 0x53756e53) {
				result.complete(true);
			} else {
				result.complete(false);
			}
		});
		return result;
	}

	private CompletableFuture<Void> readNextBlock(int startAddress) {
		final CompletableFuture<Void> finished = new CompletableFuture<Void>();
		this.readElementsOnceTyped(new UnsignedWordElement(startAddress), new UnsignedWordElement(startAddress + 1))
				.thenAccept(values -> {
					int blockId = values.get(0);

					// END_OF_MAP
					if (blockId == 0xFFFF) {
						finished.complete(null);
						return;
					}

					// Handle SunSpec Block
					int length = values.get(1);

					// Is this SunSpecModel block supported?
					SunSpecModel sunSpecModel = null;
					try {
						sunSpecModel = SunSpecModel.valueOf("S_" + blockId);
					} catch (IllegalArgumentException e) {
						System.out.println("Unknown Block with ID " + blockId);
					}

					// Read block
					final CompletableFuture<Void> readBlockFuture;
					if (sunSpecModel != null) {
						readBlockFuture = this.readBlock(startAddress, sunSpecModel);
					} else {
						readBlockFuture = CompletableFuture.completedFuture(null);
					}

					int nextBlockStartAddress = startAddress + 2 + length;
					final CompletableFuture<Void> readNextBlockFuture = this.readNextBlock(nextBlockStartAddress);

					CompletableFuture.allOf(readBlockFuture, readNextBlockFuture).thenRun(() -> {
						finished.complete(null);
					});
				});
		return finished;
	}

	/**
	 * Reads the block starting from startAddress.
	 * 
	 * @param startAddress the address to start reading from
	 * @param model        the SunSpecModel
	 */
	private CompletableFuture<Void> readBlock(int startAddress, SunSpecModel model) {
		final CompletableFuture<Void> finished = new CompletableFuture<Void>();
		AbstractModbusElement<?>[] elements = new AbstractModbusElement[model.points.length];
		startAddress += 2;
		for (int i = 0; i < model.points.length; i++) {
			Point point = model.points[i];
			AbstractModbusElement<?> element = point.get().generateModbusElement(startAddress);
			startAddress += element.getLength();
			elements[i] = element;
		}

		this.readElementsOnce(elements).thenAccept(values -> {
			/*
			 * Use results to prepare final Modbus Task
			 * 
			 * -> register Channels to defined SunSpec points
			 * 
			 * -> ignore non-defined SunSpec points with DummyElement
			 */
			System.out.println("BLOCK #" + model.name());
			for (int i = 0; i < values.size(); i++) {
				Point point = model.points[i];
				Object value = values.get(i);
				AbstractModbusElement<?> element = elements[i];
				if (point.isDefined(value)) {
					// Point is available -> create Channel
					SunSChannelId<?> channelId = point.getChannelId();
					this.addChannel(channelId);
					element = m(channelId, element);

				} else {
					// Point is not available -> replace element with dummy
					element = new DummyRegisterElement(element.getStartAddress(),
							element.getStartAddress() + point.get().type.length - 1);
				}
			}
			final Task task = new FC3ReadRegistersTask(elements[0].getStartAddress(), Priority.HIGH, elements);
			this.modbusProtocol.addTask(task);
			finished.complete(null);
		});
		return finished;
	}

	private <T> CompletableFuture<T> readELementOnce(AbstractModbusElement<T> element) {
		// Prepare result
		final CompletableFuture<T> result = new CompletableFuture<T>();

		// Activate task
		final Task task = new FC3ReadRegistersTask(element.getStartAddress(), Priority.HIGH, element);
		this.modbusProtocol.addTask(task);

		// Register listener for element
		element.onUpdateCallback(value -> {
			if (value == null) {
				// try again
				return;
			}
			// do not try again
			this.modbusProtocol.removeTask(task);
			result.complete(value);
		});

		return result;
	}

	/**
	 * Reads given Elements once from Modbus.
	 * 
	 * @param <T>      the Type of the elements
	 * @param elements the elements
	 * @return a future list with the values, e.g. a list of integers
	 */
	@SafeVarargs
	private final <T> CompletableFuture<List<T>> readElementsOnceTyped(AbstractModbusElement<T>... elements) {
		// Register listeners for elements
		@SuppressWarnings("unchecked")
		final CompletableFuture<T>[] subResults = (CompletableFuture<T>[]) new CompletableFuture<?>[elements.length];
		for (int i = 0; i < elements.length; i++) {
			CompletableFuture<T> subResult = new CompletableFuture<T>();
			subResults[i] = subResult;

			AbstractModbusElement<T> element = elements[i];
			element.onUpdateCallback(value -> {
				if (value == null) {
					// try again
					return;
				}
				subResult.complete(value);
			});
		}

		// Activate task
		final Task task = new FC3ReadRegistersTask(elements[0].getStartAddress(), Priority.HIGH, elements);
		this.modbusProtocol.addTask(task);

		// Prepare result
		final CompletableFuture<List<T>> result = new CompletableFuture<List<T>>();
		CompletableFuture.allOf(subResults).thenRun(() -> {
			// do not try again
			this.modbusProtocol.removeTask(task);

			// get all results and complete result
			List<T> values = Stream.of(subResults) //
					.map(future -> future.join()) //
					.collect(Collectors.toCollection(ArrayList::new));
			result.complete(values);
		});

		return result;
	}

	/**
	 * Reads given Elements once from Modbus. This is a non-generic version.
	 * 
	 * @param elements the elements
	 * @return a future list with the values as Object
	 */
	@SafeVarargs
	private final CompletableFuture<List<?>> readElementsOnce(AbstractModbusElement<?>... elements) {
		// Register listeners for elements
		final CompletableFuture<?>[] subResults = new CompletableFuture<?>[elements.length];
		for (int i = 0; i < elements.length; i++) {
			CompletableFuture<Object> subResult = new CompletableFuture<>();
			subResults[i] = subResult;

			AbstractModbusElement<?> element = elements[i];
			element.onUpdateCallback(value -> {
				if (value == null) {
					// try again
					return;
				}
				subResult.complete(value);
			});
		}

		// Activate task
		final Task task = new FC3ReadRegistersTask(elements[0].getStartAddress(), Priority.HIGH, elements);
		this.modbusProtocol.addTask(task);

		// Prepare result
		final CompletableFuture<List<?>> result = new CompletableFuture<List<?>>();
		CompletableFuture.allOf(subResults).thenRun(() -> {
			// do not try again
			this.modbusProtocol.removeTask(task);

			// get all results and complete result
			List<?> values = Stream.of(subResults) //
					.map(future -> future.join()) //
					.collect(Collectors.toCollection(ArrayList::new));
			result.complete(values);
		});

		return result;
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public String debugLog() {
		Optional<Channel<?>> c = this.getSunSpecChannel(SunSpecModel.S122.TMS);
		if (c.isPresent()) {
			return c.get().address() + ":" + c.get().value();
		}
		return "Channels:" + this.channels().size();
//		return "L:" + this.getActivePower().value().asString();
	}

	protected Optional<Channel<?>> getSunSpecChannel(Point point) {
		try {
			return Optional.ofNullable(this.channel(point.getChannelId()));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}
}
