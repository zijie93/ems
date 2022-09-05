export const TRANSLATION = {
    General: {
        active: 'Activo',
        actualPower: 'e-car Carga',
        autarchy: 'Autosuficiencia',
        automatic: 'Automático',
        cancel: 'cancelar',
        capacity: 'Capacidad',
        changeAccepted: 'Cambio aceptado',
        changeFailed: 'Cambio fallido',
        chargeDischarge: 'Débito/Descarga',
        chargePower: 'Carga',
        componentCount: 'Numero de componentes',
        componentInactive: 'El componente está inactivo!',
        connectionLost: 'Conexión perdida. Intentando reconectar.',
        consumption: 'Consumo',
        cumulative: 'Valores Acumulativos',
        currentValue: 'Valor actual',
        dateFormat: 'dd.MM.yyyy', // e.g. German: dd.MM.yyyy, English: yyyy-MM-dd (dd = Day, MM = Month, yyyy = Year)
        digitalInputs: 'Entradas digitales',
        numberOfComponents: 'número de componentes',
        directConsumption: 'Consumo directo',
        dischargePower: 'Descarga',
        fault: 'Error',
        grid: 'Red',
        gridBuy: 'Relación',
        gridBuyAdvanced: 'Relación',
        gridSell: 'Fuente de alimentación',
        gridSellAdvanced: 'Fuente de alimentación',
        history: 'Historia',
        inactive: 'Inactivo',
        info: 'Informacion',
        inputNotValid: 'Entrada inválida',
        insufficientRights: 'Derechos insuficientes',
        live: 'Live',
        load: 'la cantidad',
        manually: 'a mano',
        measuredValue: 'Valor Medido',
        mode: 'Modo',
        more: 'Más...',
        noValue: 'Sin valor',
        off: 'Apagado',
        offGrid: 'No hay conexión de red',
        ok: 'ok',
        On: 'Conmutada',
        otherConsumption: 'otro Consumo',
        percentage: 'Por ciento',
        periodFromTo: 'de {{value1}} para {{value2}}', // value1 = beginning date, value2 = end date
        phase: 'Fase',
        phases: 'Fases',
        power: 'Rendimiento',
        production: 'Producción',
        reportValue: 'Reportar datos corruptos',
        search: 'Búsqueda',
        selfConsumption: 'Autoconsumo',
        soc: 'Cargo',
        state: 'Estado',
        storageSystem: 'Almacenamiento',
        systemState: 'Estado del sistema',
        total: 'consumo total',
        totalState: 'Condición general',
        warning: 'Advertencia',
        Week: {
            monday: 'Lunes',
            tuesday: 'Martes',
            wednesday: 'Miércoles',
            thursday: 'Jueves',
            friday: 'Viernes',
            saturday: 'Sábado',
            sunday: 'Domingo'
        },
        Month: {
            january: 'Enero',
            february: 'Febrero',
            march: 'Marzo',
            april: 'Abril',
            may: 'Mayo',
            june: 'Junio',
            july: 'Julio',
            august: 'Agosto',
            september: 'Septiembre',
            october: 'Octubre',
            november: 'Noviembre',
            december: 'Diciembre',
        },
    },
    Menu: {
        aboutUI: 'Sobre OpenEMS',
        accessLevel: 'Nivel de acceso',
        edgeSettings: 'Configuración OpenEMS',
        generalSettings: 'Configuración general',
        index: 'Visión general',
        logout: 'Desuscribirse',
        menu: 'Menú',
        name: 'Nombre',
        overview: 'estudio OpenEMS',
        settings: 'Ajustes',
        user: 'Usuario',
    },
    Index: {
        allConnected: 'Todas las conexiones establecidas.',
        connectionInProgress: 'Establecimiento de la conexión...',
        connectionFailed: 'Conexión a {{value}} seperados.', // value = name of websocket
        connectionSuccessful: 'Conexión a {{value}} hecho.', // value = name of websocket
        deviceOffline: 'El aparato no está conectado!',
        isOffline: 'OpenEMS está fuera de línea!',
        loggedInAs: 'Conectado como:',
        toEnergymonitor: 'Al monitor de energía...',
        type: 'Tipo:'
    },
    Login: {
        title: "Login",
        preamble: "Por favor, introduzca su contraseña o confirme la entrada por defecto para conectarse como invitado.",
        passwordLabel: "Contraseña",
        passwordReset: "Restablecer contraseña",
        authenticationFailed: "Fallo de autentificación",
    },
    Register: {
        title: "Crear una cuenta de usuario",
        segment: {
            user: "Usuario",
            installer: "Instalador"
        },
        form: {
            user: {
                acceptPrivacyPolicy: "Al crear una cuenta de FENECON, declaro que he leído y acepto la <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Política de Privacidad</a> y las Condiciones de Uso de FENECON.*",
            },
            installer: {
                acceptPrivacyPolicy: "Al crear una cuenta de instalador de FENECON, declaro que he leído y acepto la <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Política de privacidad</a> y las Condiciones de uso de FENECON.*",
            },
            contactDetails: "Datos de contacto",
            companyName: "Nombre de la empresa",
            firstname: "Nombre",
            lastname: "Apellido",
            street: "Calle | número de casa",
            zip: "Código postal",
            city: "Ciudad",
            country: "País",
            phone: "Número de teléfono",
            email: "Correo electrónico",
            password: "Contraseña",
            confirmPassword: "Confirmar contraseña",
            isElectrician: "Confirmo que mi empresa está inscrita en el registro de instaladores y, por tanto, estoy autorizado a conectar y poner en marcha un sistema de almacenamiento.",
            acceptPrivacyPolicy: "Al crear una cuenta de instalador de FENECON, declaro que he leído y acepto la  <a target=\"_blank\" href=\"https://fenecon.de/page/datenschutzerklaerung/\">Política de privacidad</a> y las condiciones de uso de FENECON.*",
            acceptAgb: "Confirmo el <a target=\"_blank\" href=\"https://fenecon.de/page/agb/\">AGB</a>.*",
            subscribeNewsletter: "Me gustaría suscribirme al boletín de FENECON para recibir todas las novedades de FENECON."
        },
        button: "Crear",
        errors: {
            requiredFields: "Por favor, rellene todos los campos",
            passwordNotEqual: "Las contraseñas no son iguales"
        },
        success: "Registro realizado con éxito"
    },
    Edge: {
        Index: {
            EmergencyReserve: {
                InfoForEmergencyReserveSlider: 'Al activar la reserva de energía de emergencia, el valor puede seleccionarse libremente entre el 5% y el 100%.',
                emergencyReserve: 'reserva de emergencia',
            },
            Energymonitor: {
                activePower: 'Potencia de salida',
                consumptionWarning: 'Consumo y productores desconocidos',
                gridMeter: 'Medidor de potencia',
                productionMeter: 'Contador de generación',
                reactivePower: 'Potencia reactiva',
                storage: 'Memoria',
                storageCharge: 'Carga del almacenaje',
                storageDischarge: 'Descarga de memoria',
                title: 'Monitor de energía',
            },
            Widgets: {
                autarchyInfo: 'La autarquía indica el porcentaje de energía actual que puede cubrirse mediante la descarga de generación y almacenamiento.',
                phasesInfo: 'La suma de las fases individuales puede diferir ligeramente del total por razones técnicas.',
                selfconsumptionInfo: 'El autoconsumo indica el porcentaje de la salida generada actualmente que puede ser utilizado por el consumo directo y la carga de almacenamiento.',
                twoWayInfoGrid: 'Los valores negativos corresponden a la inyección a la red, Los valores positivos corresponden a la alimentación de la red',
                InfoStorageForCharge: 'Los valores negativos corresponden a la carga de memoria',
                InfoStorageForDischarge: 'Los valores positivos corresponden a la descarga de la memoria', Channeltreshold: {
                    output: 'Salida'
                },
                Singlethreshold: {
                    above: 'Sobre',
                    behaviour: 'Comportamiento',
                    below: 'Por debajo',
                    currentValue: 'Valor actual',
                    dependendOn: 'Dependiendo de',
                    minSwitchingTime: 'Conmutación mínimo',
                    moreThanMaxPower: 'El valor no debe ser inferior a la potencia máxima del dispositivo controlado',
                    other: 'Otro',
                    relationError: 'El umbral debe ser mayor que la carga conmutada',
                    switchedLoadPower: 'Carga conmutada',
                    switchOffAbove: 'Apagar a través de',
                    switchOffBelow: 'Apagar bajo',
                    switchOnAbove: 'Encender a través de',
                    switchOnBelow: 'Encender debajo',
                    threshold: 'Thresholded',
                },
                DelayedSellToGrid: {
                    sellToGridPowerLimit: 'Cargar arriba',
                    continuousSellToGridPower: 'Descarga a continuación',
                    relationError: 'El límite de carga debe ser mayor que el límite de descarga',
                },
                Peakshaving: {
                    asymmetricInfo: 'Los valores de rendimiento introducidos se refieren a fases individuales. Se ajusta a la fase más estresada.',
                    endDate: 'Fecha final',
                    endTime: 'Hora de finalización',
                    mostStressedPhase: 'Fase mayormente estresada',
                    peakshaving: 'Afeitado máximo',
                    peakshavingPower: 'Descarga sobre',
                    recharge: 'Poder de carga',
                    rechargePower: 'Cargando debajo',
                    relationError: 'Límite de descarga debe ser mayor o igual que el límite de carga',
                    startDate: 'Fecha de inicio',
                    startTime: 'Hora de inicio',
                    startTimeCharge: 'Hora de inicio de carga',
                },
                GridOptimizedCharge: {
                    chargingDelayed: 'Retraso en la carga',
                    considerGridFeedInLimit: 'Tenga en cuenta la alimentación máxima a la red',
                    endTime: 'Hora de finalización',
                    endTimeDescription: 'La carga no se realiza con la cantidad máxima durante unas pocas horas, sino de manera constante durante un período de tiempo más largo.',
                    endTimeDetailedDescription: 'La carga no se realiza con la salida máxima durante unas horas, sino de manera constante con un máximo de {{value1}} a {{value2}} en punto.', // value1 = maximum charge, value2 = end time
                    endTimeLong: 'Hora de finalización de la carga restringida',
                    expectedSoc: 'Estado de carga esperado',
                    expectedSocWithoutSellToGridLimit: 'Sin evitar la máxima inyección a la red',
                    gridFeedInLimitationAvoided: 'Se evitó una limitación de la fuente de alimentación de la red',
                    gridOptimizedChargeDisabled: 'Carga optimizada de red desactivada',
                    high: 'Alto',
                    History: {
                        batteryChargeGridLimitDescription: 'La inyección máxima a la red, por encima de la cual se incrementa la carga de las baterías (en la medida de lo posible), está por debajo de la inyección máxima a la red permitida para evitar una reducción prematura de la producción.',
                        priorityDescription: 'La carga mínima de la batería tiene mayor prioridad que la carga máxima de la batería',
                    },
                    low: 'Bajo',
                    maximumCharge: 'Carga máxima',
                    maximumGridFeedIn: 'Inyección de red máxima permitida',
                    medium: 'Medio',
                    minimumCharge: 'Carga mínima',
                    RiskDescription: {
                        Low: {
                            functionDescription: 'Carga relativamente temprana de la tienda',
                            storageDescription: 'Mayor probabilidad de que la memoria se cargue por completo',
                            pvCurtail: 'Menor probabilidad de que se evite la reducción del sistema fotovoltaico',
                        },
                        Medium: {
                            functionDescription: 'Carga comparativamente uniforme de la tienda',
                            storageDescription: 'Alta probabilidad de que el almacenamiento esté completamente cargado',
                            pvCurtail: '',
                        },
                        High: {
                            functionDescription: 'Carga comparativamente posterior del sistema de almacenamiento',
                            storageDescription: 'Menos probabilidad de que el sistema de almacenamiento se cargue por completo',
                            pvCurtail: 'Mayor probabilidad de que se evite la reducción del sistema fotovoltaico',
                        },
                    },
                    riskPropensity: 'Propensión al riesgo',
                    settingOnlyVisableForInstaller: 'Esta configuración solo es visible para el instalador',
                    State: {
                        avoidLowCharging: 'Se evita la carga baja',
                        chargeLimitActive: 'Límite de carga activo',
                        endTimeNotCalculated: 'Hora de finalización no calculada',
                        gridFeedInLimitationIsAvoided: 'Se evita la limitación de la alimentación a la red',
                        noLimitActive: 'Sin límite de carga activo',
                        noLimitPossible: 'Sin limitación posible (restringido por controles con mayor prioridad)',
                        notDefined: 'No definida',
                        passedEndTime: 'Se superó el tiempo de finalización de la carga limitada',
                        storageAlreadyFull: 'Memoria ya llena',
                    },
                    storageCapacity: 'capacidad de almacenamiento (sólo visible para el administrador)'
                },
                CHP: {
                    highThreshold: 'Umbral alto',
                    lowThreshold: 'Umbral bajo',
                },
                EVCS: {
                    activateCharging: 'Activar la estación de carga.',
                    amountOfChargingStations: 'Cantidad de estaciones de carga',
                    cable: 'Cable',
                    cableNotConnected: 'El cable no esta conectado',
                    carFull: 'El carro esta lleno',
                    chargeLimitReached: 'Límite de carga alcanzado',
                    chargeMode: 'Modo de carga',
                    chargeTarget: 'Objetivo de carga',
                    charging: 'Inicio de Carga',
                    chargingLimit: 'Límite de carga',
                    chargingPower: 'Energía de carga',
                    chargingStation: 'Carga',
                    chargingStationCluster: 'Grupo de estaciones de carga',
                    chargingStationDeactivated: 'Estación de carga desactivada',
                    chargingStationPluggedIn: 'Estación de carga encufada',
                    chargingStationPluggedInEV: 'Estación de carga + e-Car enchufado',
                    chargingStationPluggedInEVLocked: 'Estación de carga + e-Car enchufado + bloqueando',
                    chargingStationPluggedInLocked: 'Estación de carga enchufada + bloqueado',
                    clusterConfigError: 'Se ha producido un error en la configuración del clúster Evcs.',
                    currentCharge: 'Carga actual',
                    energySinceBeginning: 'Energía desde el último inicio de carga',
                    energyLimit: 'Límite de la energía',
                    enforceCharging: 'Forzar la carga',
                    error: 'Error',
                    maxEnergyRestriction: 'Limite la energía máxima por carga',
                    notAuthorized: 'No autorizado',
                    notCharging: 'No cobrar',
                    notReadyForCharging: 'No está liesto para la carga',
                    overviewChargingStations: 'Resumen de estaciones de carga',
                    prioritization: 'Priorización',
                    readyForCharging: 'Listo para cargar',
                    starting: 'Comenzó',
                    status: 'Status',
                    totalCharge: 'Carga total',
                    totalChargingPower: 'Potencia de carga total',
                    unknown: 'Desconocido',
                    unplugged: 'No conectado',
                    Administration: {
                        carAdministration: 'Administración de automóviles',
                        customCarInfo: 'Si este es el caso, su automóvil solo puede cargarse de manera eficiente desde una determinada salida. Con este botón, esto se incluye en sus opciones de configuración, así como en la carga automática.',
                        renaultZoe: '¿Se carga principalmente un Renault Zoe en esta estación de carga?'
                    },
                    NoConnection: {
                        description: 'No se pudo conectar a la estación de carga.',
                        help1_1: 'La IP de la estación de carga aparece cuando se enciende nuevamente',
                        help1: 'Compruebe si la estación de carga está encendida y se puede acceder a ella a través de la red',
                    },
                    OptimizedChargeMode: {
                        info: 'En este modo, la carga del automóvil se ajusta a la producción y consumo actuales.',
                        minChargePower: 'velocidad de carga',
                        minCharging: 'Garantía de carga mínima',
                        minInfo: 'Si desea evitar que el automóvil no se cargue por la noche, puede establecer un cargo mínimo.',
                        name: 'Carga optimizada',
                        shortName: 'automáticamente',
                        ChargingPriority: {
                            car: 'Coche',
                            info: 'Dependiendo de la priorización, el componente seleccionado se cargará primero',
                            storage: 'Almacenamiento',
                        }
                    },
                    ForceChargeMode: {
                        info: 'En este modo se aplica la carga del automóvil, i. Siempre se garantiza que el automóvil se cargará, incluso si la estación de carga necesita acceder a la red eléctrica.',
                        maxCharging: 'Fuerza de carga maxima:',
                        maxChargingDetails: 'Si el automóvil no puede cargar el valor máximo introducido, la potencia se limita automáticamente.',
                        name: 'Carga forzada',
                        shortName: 'a mano',
                    }
                },
                Heatingelement: {
                    activeLevel: 'Fases level',
                    endtime: 'Los días pasados',
                    energy: 'Energía',
                    heatingelement: 'Elemento de calefacción',
                    minimalEnergyAmount: 'Cantidad mínima de energía',
                    minimumRunTime: 'Plazo mínimo',
                    priority: 'Prioridad',
                    time: 'Tiempo',
                    timeCountdown: 'Hora de empezar',
                },
                HeatPump: {
                    aboveSoc: 'y sobre el estado de carga de',
                    belowSoc: 'y bajo el estado de cargo de',
                    gridBuy: 'De la compra de la red de',
                    gridSell: 'Por exceso de alimentación de',
                    lock: 'Bloquear',
                    moreThanHpPower: 'El valor no debe ser inferior a la potencia máxima de la bomba de calor',
                    normalOperation: 'Operación normal',
                    relationError: 'El valor de exceso del comando de encendido debe ser mayor que el valor recomendado de encendido',
                    switchOnCom: 'Mando de encendido',
                    switchOnRec: 'Recomendación de encendido',
                    undefined: 'Indefinido',
                    normalOperationShort: 'Normal',
                    switchOnComShort: 'Mando',
                    switchOnRecShort: 'Recomendación',
                },
                TimeOfUseTariff: {
                    currentTariff: 'Precio actual',
                    delayedDischarge: 'Retraso en el alta',
                    storageDischarge: 'Descarga de almacenamiento',
                    State: {
                        notStarted: 'El controlador aún no se ha iniciado ',
                        delayed: 'Retraso',
                        allowsDischarge: 'Liberado',
                        standby: 'Standby',
                    },
                },
            }
        },
        History: {
            activeDuration: 'duración activa',
            beginDate: 'Seleccionar fecha de inicio',
            day: 'Día',
            endDate: 'Seleccionar fecha de finalización',
            export: 'descargar como archivo de excel',
            go: 'Nwo!',
            lastMonth: 'El me pasado',
            lastWeek: 'La semana pasada',
            lastYear: 'El año pasado',
            month: 'Mes',
            noData: 'sin datos disponibles',
            otherPeriod: 'Otro período',
            period: 'Período',
            selectedDay: '{{value}}',
            selectedPeriod: 'Período seleccionado: ',
            today: 'Hoy',
            week: 'Semana',
            year: 'Año',
            yesterday: 'Ayer',
            sun: 'Dom',
            mon: 'Lun',
            tue: 'Mar',
            wed: 'Mié',
            thu: 'Jue',
            fri: 'Vie',
            sat: 'Sáb',
            jan: 'Ene',
            feb: 'Feb',
            mar: 'Mar',
            apr: 'Abr',
            may: 'May',
            jun: 'Jun',
            jul: 'Jul',
            aug: 'Ago',
            sep: 'Sep',
            oct: 'Oct',
            nov: 'Nov',
            dec: 'Dic'
        },
        Config: {
            Index: {
                addComponents: 'Instalar componentes',
                adjustComponents: 'Configurar componentes',
                bridge: 'Conexiones y dispositivos',
                controller: 'Aplicaciones',
                dataStorage: 'Almacenamiento de datos',
                executeSimulator: 'Ejecutar simulaciones',
                liveLog: 'Protocolos de sistema de vida',
                log: 'Registro',
                manualControl: 'Control manual',
                renameComponents: 'Renombrar componentes',
                scheduler: 'Planificador de aplicaciones',
                simulator: 'Simulador',
                systemExecute: 'Ejecutar comando del sistema',
                systemProfile: 'Perfil del Sistema',
                alerting: 'Alerta',
            },
            More: {
                manualCommand: 'Comando manual',
                manualpqPowerSpecification: 'Especificaciones de rendimiento',
                manualpqReset: 'Restablecer',
                manualpqSubmit: 'Tomar',
                refuInverter: 'REFU Inversor',
                refuStart: 'Empezar',
                refuStartStop: 'Iniciar/detener inversor',
                refuStop: 'Parada',
                send: 'Enviar',
            },
            Scheduler: {
                always: 'Siempre',
                class: 'Clase:',
                contact: 'Eso no debería suceder. Póngase es contacto con <a href=\'mailto:{{value}}\'>{{value}}</a>.',
                newScheduler: 'Nuevo programador...',
                notImplemented: 'Formulario no implementado: ',
            },
            Log: {
                automaticUpdating: 'Actualización automática',
                level: 'Nivel',
                message: 'Mensaje',
                source: 'Ésos',
                timestamp: 'Hora',
            },
            Controller: {
                app: 'Aplicación:',
                internallyID: 'Interno ID:',
                priority: 'Priodad:',
            },
            Bridge: {
                newConnection: 'Nueva conexión...',
                newDevice: 'Nuevo dispositivo...',
            },
            Alerting: {
                activate: 'Activar',
                delay: 'Retraso',
                save: 'Guardar',
                options: {
                    15: '15 minutos',
                    60: '1 hora',
                    1440: '1 día'
                },
                toast: {
                    success: 'Ajustes adoptados',
                    error: 'Error al cargar la configuración'
                },
            },
            App: {
                header: 'El App Manager se encuentra actualmente en una primera versión de prueba. Si no se muestran todas las aplicaciones, es posible que haya que actualizar la versión de FEMS.',
                installed: 'Instalado',
                available: 'Disponible',
                incompatible: 'Incompatible',
                buyApp: 'Comprar aplicación',
                modifyApp: 'Modificar la aplicación',
                createApp: 'Instalar la aplicación',
                deleteApp: 'Eliminar la aplicación',
                updateApp: 'Actualizar la aplicación',
                errorInstallable: 'Errores de instalación',
                errorCompatible: 'Errores de compatibilidad',
            },
        },
        Service: {
            entireSystem: "Sistema entero",
            Cell: {
                voltages: "Voltajes de celda",
                temperatures: "temperaturas de celda",
                insulation: "Aislamiento",
            }
        }
    },
    About: {
        build: "Esta compilación",
        contact: "Para preguntas y sugerencias sobre el sistema, por favor contacte a nuestro FEMS-Team en <a href=\"mailto:{{value}}\">{{value}}</a>.",
        currentDevelopments: "Desarrollos actuales",
        developed: "Esta interfaz de usario es desarrollada por OpenEMS como software de código abierto.",
        faq: "Preguntas frecuentes (FAQ)",
        language: "Seleccionar idioma:",
        openEMS: "Acerca de OpenEMS",
        patchnotes: "Cambios en la supervisión de esta compilación",
        ui: "Interfaz de usario para OpenEMS",
    },
    Notifications: {
        authenticationFailed: 'Sin conexión: error de autenticación.',
        closed: 'Conexión terminada.',
        failed: 'Error al configurar la conexión.',
        loggedIn: 'Registrado.',
        loggedInAs: 'Conectado como usuario \'{{value}}\'.', // value = username
    }
}
