from pulp import LpProblem, LpMinimize, LpVariable, lpSum, LpStatus, value

def resolver_asignacion():
    # 1. DATOS DE ENTRADA (Simulación para el modelo)
    instructores = ['Carlos', 'Miguel', 'Ana']
    clases = ['Yoga', 'Crossfit', 'Spinning', 'Pilates', 'Zumba', 'Boxeo']

    # Costo por hora de cada instructor según la disciplina
    costos_por_hora = {
        'Carlos': {'Yoga': 15, 'Crossfit': 20, 'Spinning': 18, 'Pilates': 15, 'Zumba': 12, 'Boxeo': 25},
        'Miguel': {'Yoga': 12, 'Crossfit': 18, 'Spinning': 15, 'Pilates': 12, 'Zumba': 10, 'Boxeo': 20},
        'Ana':    {'Yoga': 20, 'Crossfit': 25, 'Spinning': 22, 'Pilates': 20, 'Zumba': 18, 'Boxeo': 30}
    }

    # Duración de cada clase en horas
    duracion_clase = {
        'Yoga': 1.5,
        'Crossfit': 1.0,
        'Spinning': 1.0,
        'Pilates': 2.0,
        'Zumba': 1.0,
        'Boxeo': 1.5
    }

    # Capacidad máxima laboral semanal por instructor
    capacidad_horas = {
        'Carlos': 4.0,
        'Miguel': 4.0,
        'Ana': 5.0
    }

    # 2. DEFINICIÓN DEL PROBLEMA
    prob = LpProblem("Asignacion_Instructores_FitZone", LpMinimize)

    # 3. VARIABLES DE DECISIÓN (x_ij es binaria: 1 si se asigna, 0 si no)
    x = LpVariable.dicts("Asignacion", 
                         [(i, j) for i in instructores for j in clases], 
                         cat='Binary')

    # 4. FUNCIÓN OBJETIVO
    # Minimizar costo total = Sumatoria(Costo por hora * Duración * Variable Asignación)
    prob += lpSum([costos_por_hora[i][j] * duracion_clase[j] * x[(i, j)] 
                   for i in instructores for j in clases]), "Costo_Total_Nomina"

    # 5. RESTRICCIONES
    # Restricción 1: Todas las clases deben tener exactamente 1 instructor asignado
    for j in clases:
        prob += lpSum([x[(i, j)] for i in instructores]) == 1, f"Cobertura_{j}"

    # Restricción 2: Los instructores no pueden superar sus horas máximas de disponibilidad
    for i in instructores:
        prob += lpSum([duracion_clase[j] * x[(i, j)] for j in clases]) <= capacidad_horas[i], f"Capacidad_{i}"

    # 6. RESOLUCIÓN
    print("Iniciando resolución del modelo matemático...")
    prob.solve()

    print(f"\nESTADO DE LA OPTIMIZACIÓN: {LpStatus[prob.status]}")

    # Imprimir resultados
    if LpStatus[prob.status] == 'Optimal':
        print("\n" + "="*45)
        print("        PLAN DE ASIGNACIÓN ÓPTIMO")
        print("="*45)
        for i in instructores:
            horas_asignadas = 0
            asignaciones_instructor = []
            
            for j in clases:
                if x[(i, j)].varValue == 1:
                    costo_clase = costos_por_hora[i][j] * duracion_clase[j]
                    horas_asignadas += duracion_clase[j]
                    asignaciones_instructor.append(f"  -> Clase: {j:<10} | Duración: {duracion_clase[j]:.1f}h | Costo de nómina: ${costo_clase:.2f}")
            
            print(f"\n- INSTRUCTOR: {i}")
            print(f"   Capacidad total: {capacidad_horas[i]:.1f}h | Horas utilizadas: {horas_asignadas:.1f}h")
            for asig in asignaciones_instructor:
                print(asig)
                    
        print("\n" + "="*45)
        print(f"COSTO MINIMO TOTAL OPERATIVO: ${value(prob.objective):.2f}")
        print("="*45 + "\n")
    else:
        print("\n[ERROR] El modelo es infactible. No hay suficientes horas disponibles entre los instructores para cubrir todas las clases. Por favor, revisa la capacidad de horas o contrata más instructores.")

if __name__ == "__main__":
    resolver_asignacion()
