import { Component } from '@angular/core';
import { HistoryUtils, Utils } from '../../../../../shared/service/utils';
import { ChannelAddress } from '../../../../../shared/shared';
import { ChannelFilter, ChartData, DisplayValues, YAxisTitle } from '../../../shared';
import { AbstractHistoryChart } from 'src/app/shared/genericComponents/chart/abstracthistorychart'

@Component({
  selector: 'productionTotalChart',
  templateUrl: '../../../../../shared/genericComponents/chart/abstracthistorychart.html',
})
export class TotalChartComponent extends AbstractHistoryChart {

  protected override getChartData(): ChartData {
    let productionMeterComponents = this.config.getComponentsImplementingNature("io.openems.edge.meter.api.SymmetricMeter").filter(component => this.config.isProducer(component));
    let chargerComponents = this.config.getComponentsImplementingNature("io.openems.edge.ess.dccharger.api.EssDcCharger");

    let chartObject: ChartData = {
      channel:
        [{
          name: 'ProductionDcActualPower',
          powerChannel: ChannelAddress.fromString('_sum/ProductionDcActualPower'),
          energyChannel: ChannelAddress.fromString('_sum/ProductionDcActiveEnergy'),
          filter: ChannelFilter.NOT_NULL,
        },
        {
          name: 'ProductionAcActivePowerL1',
          powerChannel: ChannelAddress.fromString('_sum/ProductionAcActivePowerL1'),
          filter: ChannelFilter.NOT_NULL,
        },
        {
          name: 'ProductionAcActivePowerL2',
          powerChannel: ChannelAddress.fromString('_sum/ProductionAcActivePowerL2'),
          filter: ChannelFilter.NOT_NULL,
        },
        {
          name: 'ProductionAcActivePowerL3',
          powerChannel: ChannelAddress.fromString('_sum/ProductionAcActivePowerL3'),
          filter: ChannelFilter.NOT_NULL,
        },
        {
          name: 'ProductionActivePower',
          powerChannel: ChannelAddress.fromString('_sum/ProductionActivePower'),
          energyChannel: ChannelAddress.fromString('_sum/ProductionActiveEnergy'),
          filter: ChannelFilter.NOT_NULL,
        },
        ],
      displayValues: (channel: { name: string, data: number[] }[]) => {
        let datasets: DisplayValues[] = [];
        datasets.push({
          name: this.showTotal == false ? this.translate.instant('General.production') : this.translate.instant('General.total'),
          setValue: () => {
            return channel.find(element => element.name == 'ProductionActivePower')?.data
          },
          color: 'rgb(0,152,204)',
          hiddenOnInit: true,
          stack: 2,
        })

        if (!this.showTotal) {
          return datasets
        }

        for (let i = 1; i < 4; i++) {
          datasets.push({
            name: "Phase L" + i,
            setValue: () => {
              if (!this.showPhases) {
                return null;
              }

              let result: number[] = [];

              if (this.config.getComponentsImplementingNature("io.openems.edge.ess.dccharger.api.EssDcCharger").length > 0) {
                channel.find(element => element.name == 'ProductionDcActualPower')?.data.forEach((value, index) => {
                  result[index] = Utils.addSafely(channel.find(element => element.name == 'ProductionAcActivePowerL' + i)?.data[index], value / 3)
                })
              } else if (this.config.getComponentsImplementingNature("io.openems.edge.meter.api.AsymmetricMeter").length > 0) {
                result = channel.find(element => element.name = 'ProductionAcActivePowerL' + i)?.data
              }
              return result ?? null
            },
            color: 'rgb(' + this.phaseColors[i - 1] + ')',
            stack: 3
          })
        }

        // ProductionMeters
        for (let component of productionMeterComponents) {
          datasets.push({
            name: component.alias ?? component.id,
            setValue: () => {
              return channel.find(element => element.name == component.id)?.data ?? null
            },
            color: 'rgb(253,197,7)',
            stack: 1
          })
        }

        let chargerColors: string[] = ['rgb(0,223,0)', 'rgb(0,178,0)', 'rgb(0,201,0)', 'rgb(0,134,0)', 'rgb(0,156,0)', 'rgb(0,112,0)', 'rgb(0,89,0)']
        // ChargerComponents
        for (let i = 0; i < chargerComponents.length; i++) {
          let component = chargerComponents[i];
          datasets.push({
            name: component.alias ?? component.id,
            setValue: () => {
              return channel.find(element => element.name == component.id)?.data ?? null
            },
            color: chargerColors[i],
            stack: 1
          })
        }
        return datasets;
      },
      tooltip: {
        formatNumber: '1.1-2'
      },
      unit: YAxisTitle.ENERGY,
    }

    for (let component of productionMeterComponents) {
      chartObject.channel.push({
        name: component.id,
        powerChannel: ChannelAddress.fromString(component.id + '/ActivePower'),
        energyChannel: ChannelAddress.fromString(component.id + '/ActivePower'),
      })

    }
    for (let component of chargerComponents) {
      chartObject.channel.push({
        name: component.id,
        powerChannel: ChannelAddress.fromString(component.id + '/ActualPower'),
        energyChannel: ChannelAddress.fromString(component.id + '/ActualEnergy'),
      })
    }

    return chartObject;
  }

  public override getChartHeight(): number {
    if (this.showTotal) {
      return window.innerHeight / 1.3;
    } else {
      return window.innerHeight / 2.3
    }
  }
}