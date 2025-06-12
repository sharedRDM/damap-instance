#!/bin/bash

extract_backend_instance() {
    local instance=$1
    
    if [ "$instance" = "MUG" ]; then
        echo "Cloning MUG backend instance"
        git clone https://github.com/sharedrdm/damap-backend.git temp-repos/mug-backend
        
        mkdir -p instances/backend/MUG
        cp -r temp-repos/mug-backend/src instances/backend/MUG/src
        cp temp-repos/mug-backend/pom.xml instances/backend/MUG/pom.xml
        
        echo "MUG backend instance extracted!"
        
    elif [ "$instance" = "TUG" ]; then
        echo "Cloning TUG backend instance"
        git clone https://github.com/tugraz-rdm/damap-backend-tugraz.git temp-repos/tug-backend
        
        mkdir -p instances/backend/TUG
        cp -r temp-repos/tug-backend/src instances/backend/TUG/src
        cp temp-repos/tug-backend/pom.xml instances/backend/TUG/pom.xml
        
        echo "TUG backend instance extracted!"
        
    else
        echo "Unknown backend instance: $instance
Available backend instances: MUG, TUG"
        exit 1
    fi
}

extract_frontend_instance() {
    local instance=$1
    
    echo "Frontend instances not yet implemented
Planned: MUG, TUG"
    exit 1
}

show_usage() {
    echo "Usage: $0 <type> [instance]

Types:
  backend     - extract instances
  frontend    - (not yet implemented)

Examples:
  $0 backend           - Extract all backend instances
  $0 backend MUG       - Extract only MUG backend instance
  $0 backend TUG       - Extract only TUG backend instance"
}

# Handle legacy usage for backward compatibility
if [ $# -eq 0 ]; then
    echo "⚠️  Legacy usage detected  -> Consider using: $0 backend"
    echo "Extracting all backend instances..."
    rm -rf instances/backend temp-repos
    mkdir -p temp-repos
    
    extract_backend_instance "MUG"
    extract_backend_instance "TUG"
    
elif [ $# -eq 1 ] && ([ "$1" = "MUG" ] || [ "$1" = "TUG" ]); then
    echo "⚠️  Legacy usage detected -> Consider using: $0 backend $1"
    echo "Extracting $1 backend instance..."
    rm -rf instances/backend/$1 temp-repos
    mkdir -p temp-repos
    
    extract_backend_instance "$1"

elif [ $# -eq 1 ] && [ "$1" = "backend" ]; then
    echo "Extracting all backend instances..."
    rm -rf instances/backend temp-repos
    mkdir -p temp-repos
    
    extract_backend_instance "MUG"
    extract_backend_instance "TUG"
    
elif [ $# -eq 2 ] && [ "$1" = "backend" ]; then
    echo "Extracting $2 backend instance..."
    rm -rf instances/backend/$2 temp-repos
    mkdir -p temp-repos
    
    extract_backend_instance "$2"
    
elif [ $# -eq 1 ] && [ "$1" = "frontend" ]; then
    echo "Extracting all frontend instances..."
    extract_frontend_instance "all"
    
elif [ $# -eq 2 ] && [ "$1" = "frontend" ]; then
    echo "Extracting $2 frontend instance..."
    extract_frontend_instance "$2"
    
else
    echo "Invalid arguments.
"
    show_usage
    exit 1
fi

rm -rf temp-repos
