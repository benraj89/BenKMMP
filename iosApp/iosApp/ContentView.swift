import SwiftUI
import shared

struct ContentView: View {
  
    let repository = Repository(databaseDriverFactory: DatabaseDriverFactory())
    
    @State var data=[Event]()
    
    var body: some View {
        NavigationView{
        
            VStack{
                List{
                    ForEach(data,id:\.id){event in
                        EventRow(event: event)
                    }
                }
            }
            .navigationTitle("Events")
        }.onAppear() {
           getEvents()
        }
    }
    
    func getEvents(){
        
        repository.getEvents(completionHandler: { events,error in
            self.data=events ?? []
        })
    }
}
