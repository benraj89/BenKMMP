//
//  RocketLaunchRow.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 25.08.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventRow: View {
    
    var event: Event

    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 10.0) {
                Text("Event name: \(event.event)")
                Text("Event Desc: \(event.event_desc)")
            }
            Spacer()
        }
    }
}
