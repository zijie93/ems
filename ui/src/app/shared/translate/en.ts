export const TRANSLATION = {
    General: {
        active: 'Active',
        actualPower: 'e-car Charge power',
        apply: 'Apply',
        autarchy: 'Autarchy',
        automatic: 'Automatically',
        cancel: 'cancel',
        capacity: 'Capacity',
        changeAccepted: 'Change accepted',
        changeFailed: 'Change failed',
        chargeDischarge: 'Charge/Discharge power',
        chargePower: 'Charge power',
        componentCount: 'Number of components',
        componentInactive: 'Component is not active!',
        connectionLost: 'Connection lost. Trying to reconnect.',
        consumption: 'Consumption',
        cumulative: 'Cumulative Values',
        currentName: 'current name',
        currentValue: 'current value',
        dateFormat: 'yyyy-MM-dd', // e.g. German: dd.MM.yyyy (dd = Day, MM = Month, yyyy = Year)
        digitalInputs: 'Digital Inputs',
        numberOfComponents: 'Number of Components',
        directConsumption: 'Direct consumption',
        dischargePower: 'Discharge power',
        fault: 'Fault',
        grid: 'Grid',
        gridBuy: 'Buy from grid',
        gridBuyAdvanced: 'Buy',
        gridSell: 'Sell to grid',
        gridSellAdvanced: 'Sell',
        history: 'History',
        inactive: 'Inactive',
        info: 'Info',
        inputNotValid: 'Input not valid',
        insufficientRights: 'Insufficient rights',
        live: 'Live',
        load: 'Load',
        manual: 'Manual',
        manually: 'Manually',
        measuredValue: 'Measured Value',
        mode: 'Mode',
        more: 'More...',
        noValue: 'No value',
        off: 'Off',
        offGrid: 'No Grid Connection!',
        ok: 'ok',
        on: 'On',
        otherConsumption: 'other Consumption',
        percentage: 'Percentage',
        periodFromTo: 'from {{value1}} to {{value2}}', // value1 = beginning date, value2 = end date
        phase: 'Phase',
        phases: 'Phases',
        power: 'Power',
        production: 'Production',
        rename: 'Rename',
        reportValue: 'Report corrupted data',
        reset: 'Reset',
        search: 'Search',
        selfConsumption: 'Self Consumption',
        soc: 'State of charge',
        state: 'State',
        storageSystem: 'Storage System',
        systemState: 'System state',
        total: 'total',
        totalState: 'Total state',
        warning: 'Warning',
        Week: {
            monday: 'Monday',
            tuesday: 'Tuesday',
            wednesday: 'Wednesday',
            thursday: 'Thursday',
            friday: 'Friday',
            saturday: 'Saturday',
            sunday: 'Sunday'
        },
        Month: {
            january: 'January',
            february: 'February',
            march: 'March',
            april: 'April',
            may: 'May',
            june: 'June',
            july: 'July',
            august: 'August',
            september: 'September',
            october: 'October',
            november: 'November',
            december: 'December',
        },
    },
    Menu: {
        aboutUI: 'About FEMS',
        accessLevel: 'Access Level',
        edgeSettings: 'FEMS Settings',
        generalSettings: 'General Settings',
        index: 'Index',
        logout: 'Sign Out',
        menu: 'Menu',
        name: 'Name',
        overview: 'All Systems',
        settings: 'Settings',
        user: 'User',
    },
    Index: {
        allConnected: 'All connections established.',
        connectionInProgress: 'Establishing connection...',
        connectionFailed: 'Connection to {{value}} failed.', // value = name of websocket
        connectionSuccessful: 'Successfully connected to {{value}}.', // value = name of websocket
        deviceOffline: 'The device is not connected!',
        isOffline: 'OpenEMS is offline!',
        loggedInAs: 'Logged in as:',
        toEnergymonitor: 'To Energymonitor...',
        type: 'Type:'
    },
    Register: {
        title: "Create user account",
        segment: {
            user: "User",
            installer: "Installer"
        },
        form: {
            user: {
                acceptPrivacyPolicy: "By creating a FENECON account, I declare that I have read and agree to the FENECON <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Privacy Policy</a> and Terms of Use.*",
            },
            installer: {
                acceptPrivacyPolicy: "By creating a FENECON installer account, I declare that I have read and agree to the FENECON <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Privacy Policy</a> and Terms of Use.*",
            },
            companyName: "Company name",
            firstname: "Firstname",
            lastname: "Lastname",
            street: "Street",
            zip: "Postal code",
            city: "City",
            country: "Country",
            phone: "Phone number",
            email: "E-Mail",
            password: "Password",
            confirmPassword: "Confirm password",
            isElectrician: "I confirm that my company is registered in the installer register and i am therefore authorised to connect and commission a storage system.",
            acceptPrivacyPolicy: "By creating a FENECON installer account, I declare that I have read and agree to the FENECON <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Privacy Policy</a> and Terms of Use.*",
            acceptAgb: "I confirm the <a target=\"_blank\" href=\"https://fenecon.de/page/agb/\">AGB</a>.*",
            subscribeNewsletter: "I would like to subscribe to the FENECON newsletter to receive all the latest news from FENECON."
        },
        button: "Create",
        errors: {
            requiredFields: "Please fill in all fields",
            passwordNotEqual: "Passwords are not equal"
        },
        success: "Registration successful"
    },
    Login: {
        title: "Login",
        preamble: "Please enter your password or submit the default value to login as a guest.",
        passwordLabel: "Password",
        passwordPlaceholder: "Password",
        passwordReset: "Reset Password",
        authenticationFailed: "Authentication Failed",
    },
    Edge: {
        Index: {
            Energymonitor: {
                activePower: 'Active power',
                consumptionWarning: 'Consumption & unknown producers',
                gridMeter: 'Grid meter',
                productionMeter: 'Production meter',
                reactivePower: 'Reactive power',
                storage: 'Storage',
                storageCharge: 'Storage-Charge',
                storageDischarge: 'Storage-Discharge',
                title: 'Energymonitor',
            },
            Widgets: {
                autarchyInfo: 'Autarky indicates the percentage of current power that can be covered by generation and storage discharge.',
                phasesInfo: 'For technical reasons, the sum of the individual phases can be slightly different from the total sum.',
                selfconsumptionInfo: 'Self-consumption indicates the percentage of the currently generated output that can be used by direct consumption and storage load itself.',
                twoWayInfoGrid: 'Negative values correspond to grid feed-in, Positive values correspond to mains supply',
                InfoStorageForCharge: 'Negative values correspond to memory loading',
                InfoStorageForDischarge: 'Positive values correspond to memory discharge',
                Channeltreshold: {
                    output: 'Output'
                },
                Singlethreshold: {
                    above: 'Above',
                    behaviour: 'Behaviour',
                    below: 'Below',
                    currentValue: 'Current value',
                    dependendOn: 'Dependend on',
                    minSwitchingTime: 'Minimum swichting time',
                    moreThanMaxPower: 'Value must not be lower than the maximum power of the controlled device',
                    other: 'Other',
                    relationError: 'Threshold must be greater than the switched load power',
                    switchedLoadPower: 'Switched load power',
                    switchOffAbove: 'Switch off above',
                    switchOffBelow: 'Switch off below',
                    switchOnAbove: 'Switch on above',
                    switchOnBelow: 'Switch on below',
                    threshold: 'Threshold',
                },
                DelayedSellToGrid: {
                    sellToGridPowerLimit: 'Charge above',
                    continuousSellToGridPower: 'Discharge below',
                    relationError: 'Charge limit must be greater than the Discharge limit',
                },
                Peakshaving: {
                    asymmetricInfo: 'The entered performance values ​​refer to individual phases. It is adjusted to the most stressed phase.',
                    endDate: 'End date',
                    endTime: 'End time',
                    mostStressedPhase: 'Most stressed phase',
                    peakshaving: 'Peak-Shaving',
                    peakshavingPower: 'Discharge above',
                    recharge: 'Recharge Power',
                    rechargePower: 'Charge below',
                    relationError: 'Discharge limit must be greater than or equal to the charge limit',
                    startDate: 'Start date',
                    startTime: 'Start time',
                    startTimeCharge: 'Charge Start time',
                },
                GridOptimizedCharge: {
                    considerGridFeedInLimit: 'Consider maximum grid feed-in',
                    endTime: 'End time',
                    endTimeDescription: 'The charging does not take place with the maximum amount for a few hours, but steadily over a longer period of time.',
                    endTimeDetailedDescription: 'The charging does not take place with the maximum amount for a few hours, but steadily with a maximum of {{value1}} to {{value2}} o\'clock.', // value1 = maximum charge, value2 = end time
                    endTimeLong: 'End time of restricted loading',
                    expectedSoc: 'Expected state of charge',
                    expectedSocWithoutSellToGridLimit: 'Without avoiding the maximum grid feed-in',
                    gridFeedInLimitationAvoided: 'Grid feed-in limitation avoided',
                    gridOptimizedChargeDisabled: 'Grid optimized charge deactivated',
                    high: 'High',
                    History: {
                        batteryChargeGridLimitDescription: 'The maximum grid feed-in, above which battery charging is increased (as far as possible) is below the maximum permitted grid feed-in in order to prevent premature production curtailment.',
                        priorityDescription: 'The minimum charge has a higher priority than the maximum charge',
                    },
                    low: 'Low',
                    maximumCharge: 'Maximum charge',
                    maximumGridFeedIn: 'Maximum allowed grid feed-in',
                    medium: 'Medium',
                    minimumCharge: 'Minimal charge',
                    RiskDescription: {
                        Low: {
                            functionDescription: 'Comparatively early charging of the storage system',
                            storageDescription: 'Higher chance that the storage system will be fully charged',
                            pvCurtail: 'Lower probability that curtailment of the PV system will be avoided',
                        },
                        Medium: {
                            functionDescription: 'Comparatively even charging of the store system',
                            storageDescription: 'High probability that the storage system will be fully charged',
                            pvCurtail: 'High probability that the curtailment of the PV system will be avoided',
                        },
                        High: {
                            functionDescription: 'Comparatively later charging of the storage system',
                            storageDescription: 'Less probability that the storage system will be fully charged',
                            pvCurtail: 'Higher probability that the curtailment of the PV system will be avoided',
                        },
                    },
                    riskPropensity: 'Risk propensity',
                    settingOnlyVisableForInstaller: 'This setting is only visible to the installer',
                    State: {
                        chargeLimitActive: 'Charge limit active',
                        endTimeNotCalculated: 'End time not calculated',
                        gridFeedInLimitationIsAvoided: 'Grid-Feed-in limitation is avoided',
                        noLimitActive: 'No charge limit active',
                        noLimitPossible: 'No limitation possible (restricted by controls with higher priority)',
                        notDefined: 'Not defined',
                        passedEndTime: 'End time of limited charging exceeded',
                        storageAlreadyFull: 'Memory already full',
                    },
                },
                CHP: {
                    highThreshold: 'High Threshold',
                    lowThreshold: 'Low Threshold',
                },
                EVCS: {
                    activateCharging: 'Activate the charging station',
                    amountOfChargingStations: 'Amount of charging stations',
                    cable: 'Cable',
                    cableNotConnected: 'Cable is not connected',
                    carFull: 'Car is full',
                    chargeLimitReached: 'Charge limit reached',
                    chargeMode: 'Charge Mode',
                    chargeTarget: 'Charge target',
                    charging: 'Is charing',
                    chargingLimit: 'Charging limit',
                    chargingPower: 'Charing power',
                    chargingStation: 'Charging Station',
                    chargingStationCluster: 'Charging station cluster',
                    chargingStationDeactivated: 'Charging station deactivated',
                    chargingStationPluggedIn: 'Charing Station plugged in',
                    chargingStationPluggedInEV: 'Charing Station + E-Vehicel plugged in',
                    chargingStationPluggedInEVLocked: 'Charing Station + E-Vehicel plugged in + locked',
                    chargingStationPluggedInLocked: 'Charing Station plugged in + locked',
                    clusterConfigError: 'An error has occurred in the configuration of the Evcs cluster',
                    currentCharge: 'Current charge',
                    energieSinceBeginning: 'Energy since the last charge start',
                    energyLimit: 'Energy Limit',
                    enforceCharging: 'Enforce charging',
                    error: 'Error',
                    maxEnergyRestriction: 'Limit maximum energy per charge',
                    notAuthorized: 'Not authorized',
                    notCharging: 'Not charging',
                    notReadyForCharging: 'Not ready for charging',
                    overviewChargingStations: 'Overview charging stations',
                    prioritization: 'Prioritization',
                    readyForCharging: 'Ready for charging',
                    starting: 'Starting',
                    status: 'Status',
                    totalCharge: 'Total charge',
                    totalChargingPower: 'Total charging power',
                    unknown: 'Unknown',
                    unplugged: 'Unplugged',
                    Administration: {
                        carAdministration: 'Car administration',
                        customCarInfo: 'If this is the case, your car can only be charged efficiently from a certain output. With this button, this is included in your configuration options as well as in the automatic charging calculations.',
                        renaultZoe: 'Is a Renault Zoe mainly charged on this charging station?'
                    },
                    NoConnection: {
                        description: 'No connection to the charging station.',
                        help1_1: 'The IP of the charging station appears when switching on again',
                        help1: 'Check if the charging station is switched on and can be reached via the network.',
                    },
                    OptimizedChargeMode: {
                        info: 'In this mode, the load of the car is adjusted to the current production and consumption.',
                        minChargePower: 'Loading rate',
                        minCharging: 'Guarantee minimum charge',
                        minInfo: 'If you want to prevent that the car is not charging at the night, you could set a minimum charge.',
                        name: 'Optimized charging',
                        shortName: 'Automatically',
                        ChargingPriority: {
                            car: 'Car',
                            info: 'Depending on the prioritization, the selected component will be loaded first',
                            storage: 'Storage',
                        }
                    },
                    ForceChargeMode: {
                        info: 'In this mode the loading of the car is enforced, i.e. it is always guaranteed that the car will be charged, even if the charging station needs to access grid power.',
                        maxCharging: 'Maximum charging power',
                        maxChargingDetails: 'If the car can not load the entered maximum value, the power will be automatically limited.',
                        name: 'Force charging',
                        shortName: 'Manually',
                    }
                },
                Heatingelement: {
                    heatingelement: 'Heating element',
                    priority: 'Priority',
                    activeLevel: 'Active level',
                    energy: 'Energy',
                    time: 'Time',
                    endtime: 'Endtime',
                    minimalEnergyAmount: 'Minimal charge amount',
                    minimumRunTime: 'Minimum runtime',
                    timeCountdown: 'time until start',
                },
                HeatPump: {
                    aboveSoc: 'and above the state of charge of',
                    belowSoc: 'and below state of charge of',
                    gridBuy: 'From grid purchase of',
                    gridSell: 'From excess feed-in of',
                    lock: 'Lock',
                    moreThanHpPower: 'The value must not be lower than the maximum output of the heat pump',
                    normalOperation: 'Normal operation',
                    normalOperationShort: 'Normal',
                    relationError: 'Switch-on command excess value must be greater than switch-on recommended value',
                    switchOnCom: 'Switch-on command',
                    switchOnComShort: 'Command',
                    switchOnRec: 'Switch-on recommendation',
                    switchOnRecShort: 'Recommendation',
                    undefined: 'Undefined',
                }
            }
        },
        History: {
            activeDuration: 'active duration',
            beginDate: 'Select Begin Date',
            day: 'Day',
            endDate: 'Select End Date',
            export: 'download as excel file',
            go: 'Go!',
            lastMonth: 'Last month',
            lastWeek: 'Last week',
            lastYear: 'Last year',
            month: 'Month',
            noData: 'No data available',
            tryAgain: 'try again later...',
            otherPeriod: 'Other period',
            period: 'Period',
            selectedDay: '{{value}}',
            selectedPeriod: 'Selected period: ',
            today: 'Today',
            week: 'Week',
            year: 'Year',
            yesterday: 'Yesterday',
            sun: 'Sun',
            mon: 'Mon',
            tue: 'Tue',
            wed: 'Wed',
            thu: 'Thu',
            fri: 'Fri',
            sat: 'Sat',
            jan: 'Jan',
            feb: 'Feb',
            mar: 'Mar',
            apr: 'Apr',
            may: 'May',
            jun: 'Jun',
            jul: 'Jul',
            aug: 'Aug',
            sep: 'Sep',
            oct: 'Oxt',
            nov: 'Nov',
            dec: 'Dec'
        },
        Config: {
            Index: {
                addComponents: 'Install components',
                adjustComponents: 'Configure components',
                bridge: 'Connections and devices',
                controller: 'Applications',
                dataStorage: 'Data Storage',
                executeSimulator: 'Execute simulations',
                liveLog: 'Live system log',
                log: 'Log',
                manualControl: 'Manual control',
                renameComponents: 'Rename components',
                scheduler: 'Applicationplanner',
                simulator: 'Simulator',
                systemExecute: 'Execute system command',
                systemProfile: 'System Profile',
            },
            More: {
                manualCommand: 'Manual command',
                manualpqPowerSpecification: 'Power specification',
                manualpqReset: 'Reset',
                manualpqSubmit: 'Submit',
                refuInverter: 'REFU Inverter',
                refuStart: 'Start',
                refuStartStop: 'Start/Stop inverter',
                refuStop: 'Stop',
                send: 'Send',
            },
            Scheduler: {
                always: 'Always',
                class: 'Class:',
                contact: 'This shouldn\'t happen. Please contact <a href=\'mailto:{{value}}\'>{{value}}</a>.',
                newScheduler: 'New scheduler...',
                notImplemented: 'Form not implemented: ',
            },
            Log: {
                automaticUpdating: 'Automatic updating',
                level: 'Level',
                message: 'Message',
                source: 'Source',
                timestamp: 'Timestamp',
            },
            Controller: {
                app: 'App:',
                internallyID: 'Internally ID:',
                priority: 'Priority:',
            },
            Bridge: {
                newConnection: 'New connection...',
                newDevice: 'New device...',
            }
        },
        Service: {
            entireSystem: "Entire system",
            Cell: {
                voltages: "Cell-voltages",
                temperatures: "Cell-temperatures",
                insulation: "Isolation",
            }
        }
    },
    About: {
        build: "This build",
        contact: "Please contact our team for further information or suggestions about the system at <a href=\"mailto:{{value}}\">{{value}}</a>.",
        currentDevelopments: "Current developments",
        developed: "This user interface is developed as open-source software.",
        faq: "Frequently asked questions (FAQ)",
        language: "Select language:",
        openEMS: "More about OpenEMS",
        patchnotes: "Changes in the monitoring for this build",
        ui: "User interface for FEMS",
    },
    Notifications: {
        authenticationFailed: 'No Connection: Authentication failed.',
        closed: 'Connection closed.',
        failed: 'Connection failed.',
        loggedIn: 'Logged in.',
        loggedInAs: 'Logged in as \'{{value}}\'.', // value = username
    },
    Role: {
        guest: 'Guest',
        owner: 'Owner',
        installer: 'Installer',
        admin: 'Administrator',
    }
}
