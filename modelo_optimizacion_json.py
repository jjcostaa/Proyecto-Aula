import json
from pulp import LpProblem, LpMinimize, LpVariable, lpSum, LpStatus, value

def resolver_asignacion():
    instructores = ['Carlos', 'Miguel', 'Ana']
    clases = ['Yoga', 'Crossfit', 'Spinning', 'Pilates', 'Zumba', 'Boxeo']

    costos_por_hora = {
        'Carlos': {'Yoga': 15, 'Crossfit': 20, 'Spinning': 18, 'Pilates': 15, 'Zumba': 12, 'Boxeo': 25},
        'Miguel': {'Yoga': 12, 'Crossfit': 18, 'Spinning': 15, 'Pilates': 12, 'Zumba': 10, 'Boxeo': 20},
        'Ana':    {'Yoga': 20, 'Crossfit': 25, 'Spinning': 22, 'Pilates': 20, 'Zumba': 18, 'Boxeo': 30}
    }

    duracion_clase = {
        'Yoga': 1.5, 'Crossfit': 1.0, 'Spinning': 1.0, 
        'Pilates': 2.0, 'Zumba': 1.0, 'Boxeo': 1.5
    }

    capacidad_horas = {
        'Carlos': 4.0, 'Miguel': 4.0, 'Ana': 5.0
    }

    prob = LpProblem("Asignacion", LpMinimize)
    x = LpVariable.dicts("Asignacion", [(i, j) for i in instructores for j in clases], cat='Binary')

    prob += lpSum([costos_por_hora[i][j] * duracion_clase[j] * x[(i, j)] for i in instructores for j in clases])

    for j in clases:
        prob += lpSum([x[(i, j)] for i in instructores]) == 1

    for i in instructores:
        prob += lpSum([duracion_clase[j] * x[(i, j)] for j in clases]) <= capacidad_horas[i]

    prob.solve()

    if LpStatus[prob.status] == 'Optimal':
        resultado = {
            "status": "Optimal",
            "costo_total": value(prob.objective),
            "asignaciones": []
        }
        for i in instructores:
            horas = 0
            clases_asignadas = []
            for j in clases:
                if x[(i, j)].varValue == 1:
                    costo = costos_por_hora[i][j] * duracion_clase[j]
                    horas += duracion_clase[j]
                    clases_asignadas.append({"clase": j, "duracion": duracion_clase[j], "costo": costo})
            
            resultado["asignaciones"].append({
                "instructor": i,
                "capacidad": capacidad_horas[i],
                "horas_utilizadas": horas,
                "clases": clases_asignadas
            })
        print(json.dumps(resultado))
    else:
        print(json.dumps({"status": "Infeasible", "mensaje": "No hay solucion optima posible"}))

if __name__ == "__main__":
    resolver_asignacion()
